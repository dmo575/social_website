package com.alfredcode.socialWebsite.DAO.exception;

public abstract class DAOException extends RuntimeException {
    public DAOException(String msg) {
        super(msg);
    }
}
