package com.alfredcode.socialWebsite.service.session.exception;

import org.springframework.http.HttpStatus;

import com.alfredcode.socialWebsite.security.exception.AuthenticationException;

public class FailedAuthenticationException extends AuthenticationException {
    public FailedAuthenticationException(String msg) {
        super(msg);
    }
    public FailedAuthenticationException(HttpStatus statusCode, String redirect, String msg) {
        super(statusCode, redirect, msg);
    }
}
