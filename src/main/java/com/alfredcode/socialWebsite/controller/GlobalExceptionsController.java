package com.alfredcode.socialWebsite.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.alfredcode.socialWebsite.security.exception.AuthenticationException;
import com.alfredcode.socialWebsite.security.exception.UnauthorizedActionException;
import com.alfredcode.socialWebsite.service.session.exception.FailedSessionAuthenticationException;
import com.alfredcode.socialWebsite.service.session.exception.FailedSessionCreationException;
import com.alfredcode.socialWebsite.service.session.exception.FailedSessionUpdateException;
import com.alfredcode.socialWebsite.service.user.exception.FailedUserAuthenticationException;

import jakarta.servlet.http.HttpServletResponse;


/*
 * Here we provide a default handling for all unchecked exceptions that might bubble up from the Controllers.
 */
@ControllerAdvice
public class GlobalExceptionsController {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionsController.class);


    // GLOBAL EXCEPTION HANDLERS --------------------------------------------------------------------

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String illegalArgumentHandler(IllegalArgumentException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<String> forbiddenActionHandler(UnauthorizedActionException ex, HttpServletResponse res){
        // log the event as INFO
        logger.info(ex.getMessage());

        return handleAuthenticationException(ex, res, "Forbidden action.", HttpStatus.FORBIDDEN);
    }



    // AUTHENTICATION & AUTHORIZATION EXCEPTION HANDLERS --------------------------------------------

    @ExceptionHandler(FailedUserAuthenticationException.class)
    public ResponseEntity<String> failedUserAuthenticationHandler(FailedUserAuthenticationException ex, HttpServletResponse res) {
        // log the event as INFO
        logger.info(ex.getMessage());
        return handleAuthenticationException(ex, res, "Invalid username/password combination.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(FailedSessionAuthenticationException.class)
    public ResponseEntity<String> failedSessionAuthenticationHandler(FailedSessionAuthenticationException ex, HttpServletResponse res) {
        // log the event as WARNING
        logger.warn(ex.getMessage());
        return handleAuthenticationException(ex, res, "Invalid session.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(FailedSessionCreationException.class)
    public ResponseEntity<String> failedSessionCreationHandler(FailedSessionCreationException ex, HttpServletResponse res){
        // log the event as ERROR
        logger.error(ex.getMessage());
        return handleAuthenticationException(ex, res, "Failure at creating a session.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FailedSessionUpdateException.class)
    public ResponseEntity<String> failedSessionUpdateHandler(FailedSessionUpdateException ex, HttpServletResponse res){
        // log the event as ERROR
        logger.error(ex.getMessage());
        return handleAuthenticationException(ex, res, "Failure at updating the session.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Theturns a ResponseEntity with values defined in the AuthenticationException if any, else it uses the optional values
     * @param ex The exception
     * @param res The servlet response
     * @param clientErrorMsg Error message to be sent to the client
     * @param defaultStatusCode Default status code to use if the exception does not contain a custom one
     * @return ResponseEntity according to the AuthenticationException's provided values
     */
    private ResponseEntity<String> handleAuthenticationException(AuthenticationException ex, HttpServletResponse res, String clientErrorMsg, HttpStatus defaultStatusCode) {

        if(ex.getRedirect()!= null) {
            res.addHeader("Location", ex.getRedirect());
        }
 
        if(ex.getStatusCode() != null) return new ResponseEntity<String>(clientErrorMsg, ex.getStatusCode());
 
        return new ResponseEntity<String>(clientErrorMsg, defaultStatusCode);
    }
}
