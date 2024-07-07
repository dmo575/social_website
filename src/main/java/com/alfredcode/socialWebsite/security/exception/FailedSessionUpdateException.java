package com.alfredcode.socialWebsite.security.exception;

public class FailedSessionUpdateException extends AuthenticationException {
    public FailedSessionUpdateException(String msg) {
        super(msg);
    }
}
