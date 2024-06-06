package com.alfredcode.socialWebsite.Controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Endpoints involved in user registration and login

@Controller
public class AccessController {

    private static final Logger logger = LoggerFactory.getLogger(AccessController.class);
    private UserService userService = new UserService();

    //////////////////////////////////////// GET
    // GET                                   GET
    //////////////////////////////////////// GET

    // registration page
    @GetMapping("/register")
    public ModelAndView getRegiser(ModelMap model, @CookieValue(value="sessionId", defaultValue="") String sessionId, HttpServletResponse res, HttpServletRequest req) {

        // if session authentication suceeds, redirect to /home
        if(Auth.authenticateSession(sessionId, res)) {

            // because the :redirect changes the location + status code, browser asks for that
            return new ModelAndView("redirect:/", model);
        }
        
        //return "forward:/register.html";
        return new ModelAndView("forward:/register.html", model);
    }

    // login page
    @GetMapping("/login")
    public ModelAndView getLogin(ModelMap model, @CookieValue(value="sessionId", defaultValue="") String sessionId, HttpServletResponse res) {

        // if session authentication succeeds, redirect to /home
        if(Auth.authenticateSession(sessionId, res)) {
            return new ModelAndView("redirect:/", model);
        }

        return new ModelAndView("forward:/login.html", model);
    }

    // REMOVE THIS ONE FROM HERE
    @GetMapping("/")
    public String home(@CookieValue(value="sessionId", defaultValue="") String sessionId, HttpServletResponse res) {

        // if unauthorized, forward to welcome.html
        if(sessionId.isEmpty() || !Auth.authenticateSession(sessionId, res)) return "forward:/welcome.html";

        return "forward:/home.html";
    }



    //////////////////////////////////////// POST
    // POST                                  POST
    //////////////////////////////////////// POST


    // registers a user and opens a session
    // 201 OK: UserModel JSON + Location to /
    // !201 OK: Error message in body
    @PostMapping("/register")
    @ResponseBody
    public UserModel postRegister(@CookieValue(value="sessionId", defaultValue="") String sessionId, HttpServletResponse res,
    RequestEntity<UserModel> req){
       
        // if client has a valid session in place, ignore registration request.
        if(!sessionId.isEmpty() && Auth.authenticateSession(sessionId, res)) throw new ForbiddenActionException("You are not allowed to register while logged in.");

        // get model
        UserModel user = req.getBody();
        
        // validate model data
        if(user == null) throw new IllegalArgumentException("User not provided.");
        if(user.getUsername() == null) throw new IllegalArgumentException("Missing JSON parameter: username");
        if(user.getPassword() == null) throw new IllegalArgumentException("Missing JSON parameter: password");


        // register user
        userService.registerUser(user);

        // create session
        Auth.setSession(user.getUsername(), res);

        // set the location header, suggests client where to go next.
        res.addHeader("Location", URL.getUrl(req, "/"));

        // let client know that the account was created
        res.setStatus(HttpServletResponse.SC_CREATED);
        return user;
    }

    // authenticates user and opens a session
    // 201 OK: UserModel JSON + Location to /
    // !201 OK: Error message in body
    @PostMapping("/login")
    @ResponseBody
    public UserModel postLogin(HttpServletResponse res, RequestEntity<UserModel> req, @CookieValue(value="sessionId", defaultValue="") String sessionId) {

        // verify that client is not already in a session (logged in)
        if(sessionId != null && Auth.authenticateSession(sessionId, res)) throw new ForbiddenActionException("You cannot try to log in while logged in.");

        // get model
        UserModel user = req.getBody();
        
        // validate model data
        if(user == null) throw new IllegalArgumentException("User not provided.");
        if(user.getUsername() == null) throw new IllegalArgumentException("Missing JSON parameter: username");
        if(user.getPassword() == null) throw new IllegalArgumentException("Missing JSON parameter: password");
        
        // authenticate user (make sure the user is valid)
        // on fail, it throws an exception with a a description of what went wrong. To be catched by a handler.
        Auth.authenticateUser(user.getUsername(), user.getPassword());

        // create a session for the user
        Auth.setSession(user.getUsername(), res);

        // set the location header, suggests client where to go next.
        res.addHeader("Location", URL.getUrl(req, "/"));

        res.setStatus(HttpServletResponse.SC_OK);
        return user; 
    }


    //////////////////////////////////////// EXCEPTION HANDLERS
    // EXCEPTION HANDLERS                    EXCEPTION HANDLERS
    //////////////////////////////////////// EXCEPTION HANDLERS

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
