package com.alfredcode.socialWebsite.exception;

public class FailedUserAuthenticationException extends RuntimeException {
    public FailedUserAuthenticationException(String msg) {
        super(msg);
    }
}
