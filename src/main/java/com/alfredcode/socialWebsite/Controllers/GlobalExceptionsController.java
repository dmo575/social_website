package com.alfredcode.socialWebsite.Controllers;


import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.alfredcode.socialWebsite.Exceptions.FailedUserAuthenticationException;
import com.alfredcode.socialWebsite.Exceptions.ForbiddenActionException;


// Holds exception handlers to be used by any endpoint
@ControllerAdvice
public class GlobalExceptionsController {
    

    // Handles an illegal argument
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String illegalArgumentHandler(IllegalArgumentException ex){
        return ex.getMessage();
    }

    // Handles a failure in user authentication
    @ExceptionHandler(FailedUserAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String failedUserAuthenticationHandler(FailedUserAuthenticationException ex){
        return ex.getMessage();
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
        return "500 - Internal Server Error (SQL)";
    }


    /*
    // Handles a failure in session authentication
    @ExceptionHandler(FailedSessionAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String failedSessionAuthenticationHandler(FailedSessionAuthenticationException ex, HttpServletResponse res){
      
        // redirect to registration page
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return ex.getRedirect();
    }
    */
}
