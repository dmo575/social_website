package com.alfredcode.socialWebsite.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alfredcode.socialWebsite.model.UserModel;
import com.alfredcode.socialWebsite.security.annotation.NoSessionAllowed;
import com.alfredcode.socialWebsite.service.session.SessionService;
import com.alfredcode.socialWebsite.service.session.exception.FailedSessionCreationException;
import com.alfredcode.socialWebsite.service.user.UserService;
import com.alfredcode.socialWebsite.tools.URL;

import jakarta.servlet.http.HttpServletResponse;

/*
 * MVC endpoints related to user registration and login.
 */

@Controller
public class AccessController {

    private static final Logger logger = LoggerFactory.getLogger(AccessController.class);
    private UserService userService = null;
    private SessionService sessionService = null;
    
    @Autowired
    public AccessController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }


    /*
     *  GET /register
     * 
     *  Serves the register view
     * 
     *  303 - If client has valid sessionId
     *  200 - If client doesnt have valid sessionId
     * 
     */
    @NoSessionAllowed(statusCode = HttpStatus.SEE_OTHER, redirect = "/")
    @GetMapping("/register")
    public ModelAndView getRegister() {

        // 200 register
        return new ModelAndView("register");
    }


    /*
     *  GET /login
     * 
     *  Serves the login view
     * 
     *  303 - If client has valid sessionId
     *  200 - If client doesnt have valid sessionId
     */
    @NoSessionAllowed(statusCode = HttpStatus.SEE_OTHER, redirect = "/")
    @GetMapping("/login")
    public ModelAndView getLogin() {

        // 200 login
        return new ModelAndView("login");
    }


    /*
     *  POST /register
     * 
     *  Registers a user and initiates a session
     * 
     *  200 - Sucessfull registration and session initiated. Location header suggests where to go next
     *  400 - If the data sent is invalid
     *  401 - If the client has a valid sessionId
     */
    @NoSessionAllowed
    @PostMapping("/register")
    public void postRegister(HttpServletResponse res, RequestEntity<UserModel> req) {

        // get user model
        UserModel user = req.getBody();
        
        // validate model data
        if(user == null) throw new IllegalArgumentException("UserModel not provided.");
        if(user.getUsername() == null) throw new IllegalArgumentException("Missing username.");
        if(user.getPassword() == null) throw new IllegalArgumentException("Missing password.");
        
        
        // register user. Throws ex on failure
        // TODO: account for failure during user registration
        userService.registerUser(user);
        
        try{
            // initiate a new session. Throws ex on failure
            res.addHeader("Set-Cookie", sessionService.initiateSession(user.getUsername()));

        } catch(FailedSessionCreationException ex) {
            // we catched this one to personalize the message for the registration context
            throw new FailedSessionCreationException("Oops. We couldn't log you into your new account. Try a manual log in.");
        }

        // return 201 and suggest /
        res.addHeader("Location", URL.getUrl(req, "/"));
        res.setStatus(HttpServletResponse.SC_CREATED);
    }


    /*
     *  POST /login
     * 
     *  Opens a session for the given user
     * 
     *  200 - Sucessfull login and session initiated. Location header suggests where to go next
     *  400 - If the data sent is invalid
     *  401 - If the client has a valid sessionId
     */
    @NoSessionAllowed
    @PostMapping("/login")
    public void postLogin(HttpServletResponse res, RequestEntity<UserModel> req) {

        // get model
        UserModel user = req.getBody();

        // validate model data
        if(user == null) throw new IllegalArgumentException("User not provided.");
        if(user.getUsername() == null) throw new IllegalArgumentException("Missing JSON parameter: username");
        if(user.getPassword() == null) throw new IllegalArgumentException("Missing JSON parameter: password");
        
        // authenticate user. Throws ex on failure
        userService.authenticateUser(user.getUsername(), user.getPassword());

        // initiate a new session. Throws ex on failure
        res.addHeader("Set-Cookie", sessionService.initiateSession(user.getUsername()));

        // return 200 and suggest /
        res.addHeader("Location", URL.getUrl(req, "/"));
        res.setStatus(HttpServletResponse.SC_OK);
    }
}
