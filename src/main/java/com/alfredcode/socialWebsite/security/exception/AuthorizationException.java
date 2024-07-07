package com.alfredcode.socialWebsite.security.exception;

/*
 * Base class for Authorization exceptions
 */
public abstract class AuthorizationException extends RuntimeException {
    public AuthorizationException(String msg) {
        super(msg);
    }
}
