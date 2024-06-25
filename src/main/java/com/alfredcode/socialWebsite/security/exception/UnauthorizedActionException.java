package com.alfredcode.socialWebsite.security.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedActionException extends AuthenticationException {
    public UnauthorizedActionException(String msg) {
        super(msg);
    }

    public UnauthorizedActionException(HttpStatus statusCode, String redirect, String msg) {
        super(statusCode, redirect, msg);
    }
}
