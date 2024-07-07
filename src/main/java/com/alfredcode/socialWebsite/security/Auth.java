package com.alfredcode.socialWebsite.security;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alfredcode.socialWebsite.security.exception.FailedSessionAuthenticationException;
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
    private void sessionRequired(JoinPoint jp) throws FailedSessionAuthenticationException {

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
        sessionService.authenticateSession(sessionId);

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
        catch(FailedSessionAuthenticationException | IllegalArgumentException ex) {
            // failing the session authentication means we do not have a valid session, so we return
            return;
        }
        
        // reaching this point means that the authentication was successful, so we prepare to throw the proper exception
        throw new UnauthorizedActionException("Valid session ID found.");
    }


}