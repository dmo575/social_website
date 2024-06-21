package com.alfredcode.socialWebsite.Controllers;


import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.function.ServerRequest.Headers;

import com.alfredcode.socialWebsite.Exceptions.FailedAuthenticationException;
import com.alfredcode.socialWebsite.Exceptions.FailedSessionAuthenticationException;
import com.alfredcode.socialWebsite.Exceptions.FailedUserAuthenticationException;
import com.alfredcode.socialWebsite.Exceptions.ForbiddenActionException;
import com.alfredcode.socialWebsite.tools.Auth;

import jakarta.servlet.http.HttpServletResponse;


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

    

    // Handles an illegal argument
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String illegalArgumentHandler(IllegalArgumentException ex){
        return ex.getMessage();
    }

    // Handles a failure in user authentication
    @ExceptionHandler(FailedUserAuthenticationException.class)
    public ResponseEntity<String> failedUserAuthenticationHandler(FailedUserAuthenticationException ex) {

        // log the event
        logger.info(ex.getMessage());

        // if we dont have a redirect path, throw a 401
        if(ex.getRedirect() == null)
            return new ResponseEntity<String>("Invalid credentials.", HttpStatus.UNAUTHORIZED);
        
        // else, throw a 303
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", ex.getRedirect());
        return new ResponseEntity<String>("Invalid credentials.", headers, HttpStatus.SEE_OTHER);
    }


    // handles requests that are not allowed by the website
    // ex: trying to register while logged in
    @ExceptionHandler(ForbiddenActionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String forbiddenActionHandler(ForbiddenActionException ex){
        return ex.getMessage();
    }

    // handles SQL exceptions
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String forbiddenActionHandler(SQLException ex){
        return "500 - Internal Server Error";
    }


    // Handles a failure in session authentication
    @ExceptionHandler(FailedAuthenticationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String failedSessionAuthenticationHandler(FailedAuthenticationException ex){

        logger.error(ex.getMessage());

        return "Oops500.";
    }

    // Handles a failure in session authentication
    @ExceptionHandler(FailedSessionAuthenticationException.class)
    public ResponseEntity<String> failedSessionAuthenticationHandler(FailedSessionAuthenticationException ex){

        // log the event
        logger.warn(ex.getMessage());

        // if we dont have a redirect path, throw a 401
        if(ex.getRedirect() == null)
            return new ResponseEntity<String>("Invalid credentials.", HttpStatus.UNAUTHORIZED);
        
        // else, throw a 303
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", ex.getRedirect());
        return new ResponseEntity<String>("Invalid credentials.", headers, HttpStatus.SEE_OTHER);
    }
}
