package com.alfredcode.socialWebsite.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.alfredcode.socialWebsite.security.exception.UnauthorizedException;
import com.alfredcode.socialWebsite.service.session.exception.FailedAuthenticationException;
import com.alfredcode.socialWebsite.service.session.exception.FailedSessionAuthenticationException;
import com.alfredcode.socialWebsite.service.session.exception.FailedSessionCreationException;
import com.alfredcode.socialWebsite.service.user.exception.FailedUserAuthenticationException;


/*
 * Here we provide a default handling for all unchecked exceptions that might bubble up from the Controllers.
 */
@ControllerAdvice
public class GlobalExceptionsController {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionsController.class);


    // GLOBAL EXCEPTION HANDLERS --------------------------------------------------------------------

    /*
     * Handles illegal argument errors
     * Ex: wrong data, missing argument
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String illegalArgumentHandler(IllegalArgumentException ex){
        return ex.getMessage();
    }

    /*
     * Handles forrbiden action errors
     * Ex: trying to register while logged in
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String forbiddenActionHandler(UnauthorizedException ex){
        return ex.getMessage();
    }



    // AUTHENTICATION & AUTHORIZATION EXCEPTION HANDLERS --------------------------------------------

    /*
     * Handles a failure in user authentication
     * Ex: Incorrect password, invalid username
     */
    @ExceptionHandler(FailedUserAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String failedUserAuthenticationHandler(FailedUserAuthenticationException ex) {

        // log the event
        logger.info(ex.getMessage());

        return "Invalid user credentials.";

    }

    /*
     * Handles a failure in session authentication, comming from a lower layer
     * Ex: Session could not be stored in the database
     */
    @ExceptionHandler(FailedAuthenticationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String failedAuthenticationHandler(FailedAuthenticationException ex){

        // log the event as ERROR
        logger.error(ex.getMessage());

        return "Something went wrong while Authenticating.";
    }

    /*
     * Handles a failure in session authentication
     * Ex: Expired session, incorrect session
     */
    @ExceptionHandler(FailedSessionAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String failedSessionAuthenticationHandler(FailedSessionAuthenticationException ex){

        // log the event as WARNING
        logger.warn(ex.getMessage());

        return "Invalid session Credentials.";
    }

    /*
     * Handles a failure in session creation.
     * Ex: Error converting Date object to HTTP date string
     */
    @ExceptionHandler(FailedSessionCreationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String failedSessionCreationHandler(FailedSessionCreationException ex){

        // log the event as ERROR
        logger.error(ex.getMessage());

        return "Failure at creating session.";

    }
}
