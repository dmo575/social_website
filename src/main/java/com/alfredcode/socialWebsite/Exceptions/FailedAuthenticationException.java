package com.alfredcode.socialWebsite.Exceptions;

public class FailedAuthenticationException extends RuntimeException {
    public FailedAuthenticationException(String msg) {
        super(msg);
    }
}
