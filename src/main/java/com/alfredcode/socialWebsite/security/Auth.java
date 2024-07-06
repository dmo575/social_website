package com.alfredcode.socialWebsite.security;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alfredcode.socialWebsite.security.annotation.NoSessionAllowed;
import com.alfredcode.socialWebsite.security.exception.UnauthorizedActionException;
import com.alfredcode.socialWebsite.service.session.SessionService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/*
 * Manages user and session authentication and authorization
 */
@Aspect
@Component
public class Auth {

    private static final Logger logger = LoggerFactory.getLogger(Auth.class);
    private SessionService sessionService = null;

    @Autowired
    public Auth(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /*
     * Throws an exception if no valid session is found in the request HTTP
    */
    @Before("@annotation(com.alfredcode.socialWebsite.security.annotation.SessionRequired)")
    private void sessionRequired(JoinPoint jp) {

        String sessionId = null;

        //get request servlet
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // get cookies if any
        Cookie[] cookies = request.getCookies();

        // if the request has cookies
        if(cookies != null) {

            // go trough them and retrieve the sessionId if any
            for(Cookie c : request.getCookies()) {
                if(c.getName().equals("sessionId")) {
                    sessionId = c.getValue();
                    break;
                }
            }
        }

        // authenticate session, throws ex on failure
        try {
            sessionService.authenticateSession(sessionId);
        }
        catch(RuntimeException ex) {

            // get method intercepted by this aspect
            Method method = retrieveMethodFromJoinPoint(jp);

            // if we fail to get the method, throw ex w/o annotation arguments passed
            if(method == null) throw new UnauthorizedActionException(ex.getMessage());

            // get annotation
            NoSessionAllowed annotation = method.getAnnotation(NoSessionAllowed.class);

            // throw exception with annotation's arguments
            throw new UnauthorizedActionException(annotation.statusCode(), annotation.redirect(), ex.getMessage());
        }


        // authorize sessionId (TODO: we dont have sequrity levels right now)
        // . . .
    }

    /*
     * Throws an exception if a valid session is found in the request HTTP
    */
    @Before("@annotation(com.alfredcode.socialWebsite.security.annotation.NoSessionAllowed)")
    private void noSessionAllowed(JoinPoint jp) throws UnauthorizedActionException {

        
        String sessionId = null;

        //get request servlet
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // get cookies if any
        Cookie[] cookies = request.getCookies();

        // if the request has cookies
        if(cookies != null) {

            // go trough them and retrieve the sessionId if any
            for(Cookie c : request.getCookies()) {
                if(c.getName().equals("sessionId")) {
                    sessionId = c.getValue();
                    break;
                }
            }
        }

        try{
            // try to validate the session
            sessionService.authenticateSession(sessionId);
        }
        catch(RuntimeException ex) {
            // an exception means failure in authentication, so we return.
            return;
        }
        
        // reaching this point means that the authentication was successful, so we prepare to throw the proper exception

        // get the method that contains the annotation that triggered this aspect method
        Method method = retrieveMethodFromJoinPoint(jp);

        // if we fail to get the method, throw ex w/o annotation arguments passed
        if(method == null) throw new UnauthorizedActionException("Valid sessionId found.");

        // get the annotation
        NoSessionAllowed annotation = method.getAnnotation(NoSessionAllowed.class);

        // throw the proper exception with the annotation values
        throw new UnauthorizedActionException(annotation.statusCode(), annotation.redirect(), "Valid sessionId found.");
    }


    /**
     * Retrieves the controller method
     * @param jp The JoinPoint from the Aspect method
     * @return The method this Aspect's method was called for. Null if no success
     */
    private Method retrieveMethodFromJoinPoint(JoinPoint jp) {

        try {
            // get the method's class
            Class<?> methodClass =  jp.getTarget().getClass();

            // get the method's signature
            MethodSignature methodSignature = (MethodSignature)jp.getSignature();
    
            // get parameters from the method's signature
            Class<?>[] methodParameters = methodSignature.getParameterTypes();
    
            // get the actual method and return it
            return methodClass.getMethod(methodSignature.getName(), methodParameters);
        }
        catch(NoSuchMethodException ex) {
            // if for some reason we fail to get the method:

            // log the error and return null
            logger.error("Failed to retrieve method from JoinPoint. Resolving to default behaivor: ", ex);

            return null;
        }
    }

}