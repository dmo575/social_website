package com.alfredcode.socialWebsite.exception;

public class FailedAuthenticationException extends RuntimeException {
    public FailedAuthenticationException(String msg) {
        super(msg);
    }
}
