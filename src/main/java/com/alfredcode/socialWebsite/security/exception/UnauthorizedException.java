package com.alfredcode.socialWebsite.security.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends AuthenticationException {
    public UnauthorizedException(String msg) {
        super(msg);
    }

    public UnauthorizedException(HttpStatus statusCode, String redirect, String msg) {
        super(statusCode, redirect, msg);
    }
}
