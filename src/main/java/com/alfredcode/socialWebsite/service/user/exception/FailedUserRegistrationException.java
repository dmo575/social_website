package com.alfredcode.socialWebsite.service.user.exception;

import org.springframework.http.HttpStatus;

import com.alfredcode.socialWebsite.security.exception.AuthenticationException;

public class FailedUserRegistrationException extends AuthenticationException {
    public FailedUserRegistrationException(String msg) {
        super(msg);
    }

    public FailedUserRegistrationException(HttpStatus statusCode, String redirect, String msg) {
        super(statusCode, redirect, msg);
    }
}
