package com.alfredcode.socialWebsite.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alfredcode.socialWebsite.security.annotation.SessionRequired;
import com.alfredcode.socialWebsite.service.session.SessionService;
import com.alfredcode.socialWebsite.service.session.exception.FailedSessionAuthenticationException;
import com.alfredcode.socialWebsite.service.session.exception.FailedSessionUpdateException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * Takes care of updating a client's session ID.
 */
public class SessionInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);
    private SessionService sessionService = null;

    @Autowired
    public SessionInterceptor(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /*
     * If the request has the @SessionRequired annotation, attempt to update the session
     * If the request doesnt have the @SessionRequired annotation, do nothing
     * 
     * Before this triggers, the Auth's aspect methods trigger. Those check if the @SessionRequired requests have a valid session or not
     * so we don't have to do it again. However the session might be expired by the time we attempt to update it. In such a case we will
     * just write it off as expired since we don't care about a couple of milliseconds of difference and this triggers PRE handle, meaning
     * we don't have to worry about reviving the session or else rolling back the changes.
     * 
     * The downside is that if someone accidentally messes with the sessionId cookie at the Controller layer, we don't have way of fixing it.
     * That could potentially log someone out.
     */
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {

        // if the handler is not a controller method, return true
        if(!(handler instanceof HandlerMethod)) return true;

        // attempt to get the annotation SessionRequired from the method
        SessionRequired sessionRequired = ((HandlerMethod)handler).getMethodAnnotation(SessionRequired.class);

        // if no @SessionRequired annotation found, then we can return true
        if(sessionRequired == null) return true;

        // at this point, we know that the client had a valid sessionId when the authentication took place.
        // it might be expired by now but it wasn't at the start, so we can try to update it if it still exists

        // get cookies
        Cookie[] cookies = req.getCookies();
        String sessionId = null;

        if(cookies != null) {

            // get sessionId
            for(Cookie c : cookies) {
                if(c.getName().equals("sessionId")) {
                    sessionId = c.getValue();
                    break;
                }
            }
        }

        try{
            // attempt to update the session
            res.setHeader("sessionId", sessionService.updateSession(sessionId));
            return true;
        }
        catch(FailedSessionUpdateException ex) {

            // if the update failed, throw ex
            throw new FailedSessionAuthenticationException("Session expired.");
        }
    }
}
