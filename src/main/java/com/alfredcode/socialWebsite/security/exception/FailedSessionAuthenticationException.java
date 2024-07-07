package com.alfredcode.socialWebsite.security.exception;

public class FailedSessionAuthenticationException extends AuthenticationException {
    public FailedSessionAuthenticationException(String msg) {
        super(msg);
    }
}
