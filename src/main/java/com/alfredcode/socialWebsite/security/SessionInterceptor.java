package com.alfredcode.socialWebsite.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alfredcode.socialWebsite.service.SessionService;

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
    private SessionService sessionService = new SessionService();

    public SessionInterceptor() {}

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView maw) throws Exception {

        // TODO: update the session ID
        logger.info("SESSION INTERCEPTOR");

    }
}
