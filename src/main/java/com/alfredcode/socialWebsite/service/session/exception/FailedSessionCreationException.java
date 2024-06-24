package com.alfredcode.socialWebsite.service.session.exception;

import org.springframework.http.HttpStatus;

import com.alfredcode.socialWebsite.security.exception.AuthenticationException;

public class FailedSessionCreationException extends AuthenticationException {
    public FailedSessionCreationException(String msg) {
        super(msg);
    }
    public FailedSessionCreationException(HttpStatus statusCode, String redirect, String msg) {
        super(statusCode, redirect, msg);
    }
}
