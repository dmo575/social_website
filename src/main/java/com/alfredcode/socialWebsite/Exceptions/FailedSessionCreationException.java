package com.alfredcode.socialWebsite.Exceptions;

public class FailedSessionCreationException extends RuntimeException {
    public FailedSessionCreationException(String msg) {
        super(msg);
    }
}
