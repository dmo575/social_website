package com.alfredcode.socialWebsite.configuration;

import com.alfredcode.socialWebsite.security.SessionInterceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/*
 * Allows me to add interceptors to the spring framework.
 * Interceptors have the ability to run before and after the controller method. They can access the request and response servlets.
 * Interceptors are used to manipulate the HTTP response/request.
 */
@Configuration
public class WebConfigurator implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // we will add the session interceptor for all endpoints
        registry.addInterceptor(new SessionInterceptor()).addPathPatterns("/**");
    }
}