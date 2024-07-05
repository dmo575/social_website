package com.alfredcode.socialWebsite.configuration;

import com.alfredcode.socialWebsite.security.SessionInterceptor;
import com.alfredcode.socialWebsite.service.session.SessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/*
 * Allows me to add interceptors to the spring framework.
 * Interceptors have the ability to run before and after the controller method. They can access the request and response servlets.
 * Interceptors are used to manipulate the HTTP response/request.
 */
@Configuration
@EnableScheduling// we use this here but we make use of the @Shcedule inside Auth.java
public class WebConfigurator implements WebMvcConfigurer {

    private SessionService sessionService = null;

    @Autowired
    public WebConfigurator(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // we will add the session interceptor for all endpoints
        registry.addInterceptor(new SessionInterceptor(sessionService)).addPathPatterns("/**");
    }
}