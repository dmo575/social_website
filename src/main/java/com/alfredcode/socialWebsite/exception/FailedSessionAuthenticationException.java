package com.alfredcode.socialWebsite.exception;

public class FailedSessionAuthenticationException extends RuntimeException{
    public FailedSessionAuthenticationException(String msg) {
        super(msg);
    }
}
