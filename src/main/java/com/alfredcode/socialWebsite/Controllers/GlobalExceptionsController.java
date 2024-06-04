package com.alfredcode.socialWebsite.Controllers;

import java.security.InvalidParameterException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.alfredcode.socialWebsite.Exceptions.FailedSessionAuthenticationException;
import com.alfredcode.socialWebsite.Exceptions.FailedUserAuthenticationException;

import jakarta.servlet.http.HttpServletResponse;

// Holds exception handlers to be used by any endpoint
@ControllerAdvice
public class GlobalExceptionsController {
    

    // Handles an incorrect parameter
    @ExceptionHandler(InvalidParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String nullUserHandler(InvalidParameterException ex){
        return ex.getMessage();
    }

    // Handles a failure in user authentication
    @ExceptionHandler(FailedUserAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String failedUserAuthenticationHandler(FailedUserAuthenticationException ex){
        return ex.getMessage();
    }

    // Handles a failure in session authentication
    @ExceptionHandler(FailedSessionAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String failedSessionAuthenticationHandler(FailedSessionAuthenticationException ex, HttpServletResponse res){

        // redirect to registration page
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return "../static/register.html";
    }
}
