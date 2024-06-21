package com.alfredcode.socialWebsite.Exceptions;

public class FailedSessionAuthenticationException extends RuntimeException{
    public FailedSessionAuthenticationException(String msg) {
        super(msg);
    }
}
