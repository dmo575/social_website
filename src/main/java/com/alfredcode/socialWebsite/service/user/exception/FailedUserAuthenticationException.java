package com.alfredcode.socialWebsite.service.user.exception;

import org.springframework.http.HttpStatus;

import com.alfredcode.socialWebsite.security.exception.AuthenticationException;

/*
 * Thrown when an error occurs while trying to authenticate the user.
 * Comes from the UserService layer.
 * Could mean a failure in either UserServie or UserDAO (DAO methods always returns null on failure for now, we throw the exceptions at the service layer)
*/
public class FailedUserAuthenticationException extends AuthenticationException {
    public FailedUserAuthenticationException(String msg) {
        super(msg);
    }
    public FailedUserAuthenticationException(HttpStatus statusCode, String redirect, String msg) {
        super(statusCode, redirect, msg);
    }
}
