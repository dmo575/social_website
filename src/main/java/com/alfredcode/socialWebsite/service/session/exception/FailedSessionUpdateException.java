package com.alfredcode.socialWebsite.service.session.exception;

import org.springframework.http.HttpStatus;

import com.alfredcode.socialWebsite.security.exception.AuthenticationException;

/*
 * Used when an error occurs at updating an existing session.
 */
public class FailedSessionUpdateException extends AuthenticationException {
    public FailedSessionUpdateException(String msg) {
        super(msg);
    }
    public FailedSessionUpdateException(HttpStatus statusCode, String redirect, String msg) {
        super(statusCode, redirect, msg);
    }
}
