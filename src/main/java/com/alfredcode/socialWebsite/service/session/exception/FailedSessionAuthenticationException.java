package com.alfredcode.socialWebsite.service.session.exception;

import org.springframework.http.HttpStatus;

import com.alfredcode.socialWebsite.security.exception.AuthenticationException;

public class FailedSessionAuthenticationException extends AuthenticationException{
    public FailedSessionAuthenticationException(String msg) {
        super(msg);
    }
    public FailedSessionAuthenticationException(HttpStatus statusCode, String redirect, String msg) {
        super(statusCode, redirect, msg);
    }
}
