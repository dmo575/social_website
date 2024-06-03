package com.alfredcode.socialWebsite.Controllers;

import java.security.InvalidParameterException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.alfredcode.socialWebsite.Exceptions.AuthenticationFailedException;

/* Holds exception handlers to be used by all controllers */
@ControllerAdvice
public class GlobalExceptionsController {
    


    // Handles an incorrect parameter
    @ExceptionHandler(InvalidParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String nullUserHandler(InvalidParameterException ex){
        return ex.getMessage();
    }

    // Handles a fail in authentication
    @ExceptionHandler(AuthenticationFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String authenticationFailedHandler(AuthenticationFailedException ex){
        return ex.getMessage();
    }
}
