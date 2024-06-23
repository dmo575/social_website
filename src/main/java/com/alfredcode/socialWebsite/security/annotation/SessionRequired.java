package com.alfredcode.socialWebsite.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.alfredcode.socialWebsite.exception.FailedAuthenticationException;

/*
 * Use on @Controller methods that require a valid session.
 */
@Retention(RetentionPolicy.RUNTIME)     // defines when the annotation is relevant? I think. Compile time, run time, all that I think
@Target(ElementType.METHOD)             // defines the target for the annotation
public @interface SessionRequired {
    // TODO: have some parent exception class instead of Throwable.
    Class<? extends Throwable> exception() default FailedAuthenticationException.class;
}
