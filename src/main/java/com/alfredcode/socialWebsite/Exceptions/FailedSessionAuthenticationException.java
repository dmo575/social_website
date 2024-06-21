package com.alfredcode.socialWebsite.Exceptions;

import lombok.Getter;

public class FailedSessionAuthenticationException extends RuntimeException{
   @Getter
    private String redirect = null;

    public FailedSessionAuthenticationException(String msg, String redirect) {
        this(msg);
        this.redirect = redirect;
    }

    public FailedSessionAuthenticationException(String msg) {
        super(msg);
    }
}
