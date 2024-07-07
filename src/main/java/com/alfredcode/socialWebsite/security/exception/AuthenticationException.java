package com.alfredcode.socialWebsite.security.exception;

/*
 * Base class for Authentication exceptions
 */
public abstract class AuthenticationException extends RuntimeException {
    public AuthenticationException(String msg) {
        super(msg);
    }
}
