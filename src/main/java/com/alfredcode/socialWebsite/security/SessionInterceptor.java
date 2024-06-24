package com.alfredcode.socialWebsite.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alfredcode.socialWebsite.model.SessionModel;
import com.alfredcode.socialWebsite.security.annotation.SessionRequired;
import com.alfredcode.socialWebsite.service.session.SessionService;
import com.alfredcode.socialWebsite.service.session.exception.FailedSessionAuthenticationException;
import com.alfredcode.socialWebsite.service.session.exception.FailedSessionUpdateException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * This interceptor handles session ID management.
 * We will use AOP for authentication and authorization (see Auth.java), but interceptors for sessionId management.
 * 
 * We could use either for both of those mentioned tasks, but we will stick with the plan above because:
 * - Separation of concerns: Interceptors are the conventional place for HTTP request/response modifications while AOP are
 * saved for cross-cutting concerns; that is tasks that involve several unrealted instances, each performing some operation.
 * - Ease of access: Interceptors make it really easy to access the HTTP req/res because they are intended to modify these things.
 * They are even included in the servlet lifecycle. With AOP you do need to do some workaround to get to the servlets.
 * - Standards: Even tho we can do these things in either place, the standard seems to be the one already explained, so going against
 * it makes the code harder to understand.
 * 
 * If there is a sessionId:
 *      - This means that
 * 
 */
public class SessionInterceptor implements HandlerInterceptor{

    private static final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);
    private SessionService sessionService = null;

    @Autowired
    public SessionInterceptor(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {

        // if the handler is not a controller method, return true
        if(!(handler instanceof HandlerMethod)) return true;

        // attempt to get the annotation SessionRequired from the method
        SessionRequired sessionRequired = ((HandlerMethod)handler).getMethodAnnotation(SessionRequired.class);

        // if no @SessionRequired annotation found, then we can return true
        if(sessionRequired == null) return true;

        // at this point, we know that the client had a valid sessionId when the authentication took place.
        // it might be expired by now but it wasn't at the start, so we can try to update it if it already exists

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

            // note: FailedSessionUpdateException right now just means that the session was deleted due to it being expired so the update
            // never happened, so we can just take it as if the session was expired and the authentication failed.
        }
    }
}
