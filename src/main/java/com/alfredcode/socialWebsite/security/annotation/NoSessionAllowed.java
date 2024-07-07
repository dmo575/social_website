package com.alfredcode.socialWebsite.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/*
 * Use on @Controller methods that do not accept a valid session (ex: login, you cant log in if you are already logged in)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NoSessionAllowed {
}