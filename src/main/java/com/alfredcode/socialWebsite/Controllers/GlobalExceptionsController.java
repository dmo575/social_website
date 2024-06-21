package com.alfredcode.socialWebsite.Controllers;


import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.alfredcode.socialWebsite.Exceptions.FailedAuthenticationException;
import com.alfredcode.socialWebsite.Exceptions.FailedSessionAuthenticationException;
import com.alfredcode.socialWebsite.Exceptions.FailedSessionCreationException;
import com.alfredcode.socialWebsite.Exceptions.FailedUserAuthenticationException;
import com.alfredcode.socialWebsite.Exceptions.ForbiddenActionException;


/*
 * Here we provide a default handling for all unchecked exceptions that might bubble out of the Controllers.
 * 
 * The idea with the unchecked exceptions is:
 * - We make them unchecked because they are already dealt with here, but a programmer have the option to catch them and handle them if preffered.
 * - We declare them in the methods that throw them to let the programmer know they can come up.
 */
@ControllerAdvice
public class GlobalExceptionsController {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionsController.class);


    // GLOBAL EXCEPTION HANDLERS --------------------------------------------------------------------

    // Handles illegal argument errors
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String illegalArgumentHandler(IllegalArgumentException ex){
        return ex.getMessage();
    }

    // Handles forrbiden action errors
    // ex: trying to register while logged in
    @ExceptionHandler(ForbiddenActionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String forbiddenActionHandler(ForbiddenActionException ex){
        return ex.getMessage();
    }



    // AUTHENTICATION & AUTHORIZATION EXCEPTION HANDLERS --------------------------------------------

    // Handles a failure in user authentication
    @ExceptionHandler(FailedUserAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String failedUserAuthenticationHandler(FailedUserAuthenticationException ex) {

        // log the event
        logger.info(ex.getMessage());

        return "Invalid user credentials.";

    }

    // Handles a failure in session authentication
    @ExceptionHandler(FailedAuthenticationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String failedAuthenticationHandler(FailedAuthenticationException ex){

        // log the event as ERROR
        logger.error(ex.getMessage());

        return "Something went wrong while Authenticating.";
    }

    // Handles a failure in session authentication
    @ExceptionHandler(FailedSessionAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String failedSessionAuthenticationHandler(FailedSessionAuthenticationException ex){

        // log the event as WARNING
        logger.warn(ex.getMessage());

        return "Invalid session Credentials.";
    }

    // Handles a failure in session authentication
    @ExceptionHandler(FailedSessionCreationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String failedSessionCreationHandler(FailedSessionCreationException ex){

        // log the event as ERROR
        logger.error(ex.getMessage());

        return "Failure at creating session.";

    }
}
