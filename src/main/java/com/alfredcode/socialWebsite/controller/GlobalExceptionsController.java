package com.alfredcode.socialWebsite.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.alfredcode.socialWebsite.DAO.exception.DAOException;
import com.alfredcode.socialWebsite.security.exception.AuthenticationException;
import com.alfredcode.socialWebsite.security.exception.AuthorizationException;
import com.alfredcode.socialWebsite.security.exception.FailedSessionAuthenticationException;

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

    /**
     * Handles DAOExceptions, which are all errors when deling with the database.
     */
    @ExceptionHandler(DAOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String failureToPersistDataHandler(DAOException ex, HttpServletResponse res){
        return "Something unexpected occured on our side. Please try again later.";
    }


    /**
     * Handles an authentication failure. Sends client to /.
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    @ResponseBody
    public String authenticationHandler(FailedSessionAuthenticationException ex, HttpServletResponse res) {

        res.setHeader("Location", "/");

        return ex.getMessage();
    }

    /**
     * Handles an authorization failure. Responds with a 401.
     */
    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String authorizationHandler(AuthorizationException ex, HttpServletResponse res) {

        return ex.getMessage();
    }

}
