package com.alfredcode.socialWebsite.security.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AuthenticationException extends RuntimeException{
    private HttpStatus statusCode = null;
    private String redirect = null;
    public AuthenticationException(String msg) {
        super(msg);
    }
    public AuthenticationException(HttpStatus statusCode, String redirect, String msg) {
        this(msg);
        this.statusCode = statusCode;
        this.redirect = redirect;
    }
}
