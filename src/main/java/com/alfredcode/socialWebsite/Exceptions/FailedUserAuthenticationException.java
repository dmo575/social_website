package com.alfredcode.socialWebsite.Exceptions;

import lombok.Getter;

public class FailedUserAuthenticationException extends RuntimeException {

    @Getter
    private String redirect = null;

    public FailedUserAuthenticationException(String msg, String redirect) {
        this(msg);
        this.redirect = redirect;
    }

    public FailedUserAuthenticationException(String msg) {
        super(msg);
    }
    
}
