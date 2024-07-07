package com.alfredcode.socialWebsite.security.exception;


public class UnauthorizedActionException extends AuthorizationException {
    public UnauthorizedActionException(String msg) {
        super(msg);
    }
}
