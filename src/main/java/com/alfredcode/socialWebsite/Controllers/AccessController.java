package com.alfredcode.socialWebsite.Controllers;


import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.alfredcode.socialWebsite.Exceptions.ForbiddenActionException;
import com.alfredcode.socialWebsite.Exceptions.UsernameTakenException;
import com.alfredcode.socialWebsite.Models.UserModel;
import com.alfredcode.socialWebsite.Services.UserService;
import com.alfredcode.socialWebsite.tools.Auth;
import com.alfredcode.socialWebsite.tools.URL;

import jakarta.servlet.http.HttpServletResponse;

// MVC endpoints related to user registration and login

@Controller
public class AccessController {

    private static final Logger logger = LoggerFactory.getLogger(AccessController.class);
    private UserService userService = new UserService();


    /*
     *  GET - /register
     *  Serves the register view
     *  Status codes:
     *  - 303: If the user is in a valid session
     *  - 200: If the user is not in a valid session
     */
    @GetMapping("/register")
    public ModelAndView getRegiser(@CookieValue(value="sessionId", defaultValue = "") String sessionId, HttpServletResponse res) {

        // if in a valid session, redirect to /
        if(Auth.authenticateSession(sessionId, res)) return new ModelAndView("redirect:/", HttpStatus.SEE_OTHER);

        // 200 register
        return new ModelAndView("register");
    }


    /*
     *  GET - /login
     *  Serves the login view
     *  Status codes:
     *  - 303: If the user is in a valid session
     *  - 200: If the user is not in a valid session
     */
    @GetMapping("/login")
    public ModelAndView getLogin(@CookieValue(value="sessionId", defaultValue = "") String sessionId, HttpServletResponse res) {

        // if in a valid session, redirect to /
        if(Auth.authenticateSession(sessionId, res)) return new ModelAndView("redirect:/", HttpStatus.SEE_OTHER);
        
        // 200 login
        return new ModelAndView("login");
    }


    /*
     *  POST - /register
     *  Registers a user and opens a session
     *  Status codes:
     *  - 200: Along a path in the Location header for redirection.
     *  - 400: If the data sent is invalid.
     *  - 401: If the user is already in a valid session.
     */
    @PostMapping("/register")
    public void postRegister(@CookieValue(value="sessionId", defaultValue = "") String sessionId, HttpServletResponse res, RequestEntity<UserModel> req) {
       
        
        // if in a valid session, throw forbiddenActionException
        if(!sessionId.isEmpty() && Auth.authenticateSession(sessionId, res)) throw new ForbiddenActionException("You cannot register while logged in.");
        
        // get model
        UserModel user = req.getBody();
        
        // validate model data. If invalid, throw IllegalArgumentException
        if(user == null) throw new IllegalArgumentException("UserModel not provided.");
        if(user.getUsername() == null) throw new IllegalArgumentException("Missing JSON parameter: username");
        if(user.getPassword() == null) throw new IllegalArgumentException("Missing JSON parameter: password");
        
        
        // register user
        userService.registerUser(user);
        
        // create session -- TODO, REWORK THIS METHOD AND OTHERS IN THE AUTH CLASS, BUBBLE UP ANYTHING THAT MODIFIES THE HTTP RESPONSE. SHOULD BE AT THE CONTROLLER LEVEL
        Auth.setSession(user.getUsername(), res);
        
        // prepare response
        res.addHeader("Location", URL.getUrl(req, "/"));
        res.setStatus(HttpServletResponse.SC_CREATED);
    }


    /*
     *  POST - /login
     *  Opens a session for the given user
     *  Status codes:
     *  - 200: Along a path in the Location header for redirection.
     *  - 400: If the data sent is invalid.
     *  - 401: If the user is already in a valid session.
     */
    @PostMapping("/login")
    public void postLogin(@CookieValue(value="sessionId", defaultValue = "") String sessionId, HttpServletResponse res, RequestEntity<UserModel> req) {

        // if in a valid session, throw forbiddenActionException
        if(!sessionId.isEmpty() && Auth.authenticateSession(sessionId, res)) throw new ForbiddenActionException("You cannot log in while logged in.");

        // get model
        UserModel user = req.getBody();

        // validate model data
        if(user == null) throw new IllegalArgumentException("User not provided.");
        if(user.getUsername() == null) throw new IllegalArgumentException("Missing JSON parameter: username");
        if(user.getPassword() == null) throw new IllegalArgumentException("Missing JSON parameter: password");
        
        // authenticate user (make sure the user is valid)
        Auth.authenticateUser(user.getUsername(), user.getPassword());

        // create a session for the user
        Auth.setSession(user.getUsername(), res);

        // prepare response
        res.addHeader("Location", URL.getUrl(req, "/"));
        res.setStatus(HttpServletResponse.SC_OK);
    }


    @ExceptionHandler(UsernameTakenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String UsernameTakenHandler(UsernameTakenException ex){
        return ex.getMessage();
    }


    //@ExceptionHandler(UserRegistrationException.class)
    //@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    //@ResponseBody
    //public String userRegistrationHandler(UserRegistrationException ex){
    //    return ex.getMessage();
    //}
}
