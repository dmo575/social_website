package com.alfredcode.socialWebsite.security.exception;

public class FailedUserAuthenticationException extends AuthenticationException {
    public FailedUserAuthenticationException(String msg) {
        super(msg);
    }
}