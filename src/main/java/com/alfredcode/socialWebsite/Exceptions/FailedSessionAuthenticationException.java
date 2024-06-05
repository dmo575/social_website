package com.alfredcode.socialWebsite.Exceptions;

public class FailedSessionAuthenticationException extends RuntimeException{
    
    private String redirect = null;

    public FailedSessionAuthenticationException(String redirect, String msg) {
        super(msg);
        this.redirect = redirect;
    }

    public String getRedirect() { return redirect; }
    
}
