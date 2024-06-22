package com.alfredcode.socialWebsite.Controllers;


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

import com.alfredcode.socialWebsite.Exceptions.FailedSessionAuthenticationException;
import com.alfredcode.socialWebsite.Exceptions.FailedSessionCreationException;
import com.alfredcode.socialWebsite.Exceptions.ForbiddenActionException;
import com.alfredcode.socialWebsite.Exceptions.UsernameTakenException;
import com.alfredcode.socialWebsite.Models.UserModel;
import com.alfredcode.socialWebsite.Services.UserService;
import com.alfredcode.socialWebsite.tools.Auth;
import com.alfredcode.socialWebsite.tools.URL;

import jakarta.servlet.http.HttpServletResponse;

/*
 * MVC endpoints related to user registration and login.
 */

@Controller
public class AccessController {

    private static final Logger logger = LoggerFactory.getLogger(AccessController.class);
    private UserService userService = new UserService();


    /*
     *  GET /register
     * 
     *  Serves the register view
     * 
     *  303 - If client has valid sessionId
     *  200 - If client doesnt have valid sessionId
     * 
     */
    @GetMapping("/register")
    public ModelAndView getRegister(@CookieValue(value="sessionId", defaultValue = "") String sessionId, HttpServletResponse res) {

        try{
            // if client has a valid sessionId: update session cookie and return 303 to /
            res.setHeader("Set-Cookie", Auth.authenticateSession(sessionId));
            return new ModelAndView("redirect:/", HttpStatus.SEE_OTHER);
        }
        catch (FailedSessionAuthenticationException ex) {
            // this means the client doesnt have a valid sessionId
        }

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
    @GetMapping("/login")
    public ModelAndView getLogin(@CookieValue(value="sessionId", defaultValue = "") String sessionId, HttpServletResponse res) {

        try{
            // if client has a valid sessionId: update session cookie and return 303 to /
            res.setHeader("Set-Cookie", Auth.authenticateSession(sessionId));
            return new ModelAndView("redirect:/", HttpStatus.SEE_OTHER);
        }
        catch (FailedSessionAuthenticationException ex) {
            // this means the client doesnt have a valid sessionId
        }

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
    @PostMapping("/register")
    public void postRegister(@CookieValue(value="sessionId", defaultValue = "") String sessionId, HttpServletResponse res, RequestEntity<UserModel> req) throws UsernameTakenException {
       
        try{
            // if the client has a valid sessionId, update session cookie an return 401
            res.setHeader("Set-Cookie", Auth.authenticateSession(sessionId));
            throw new ForbiddenActionException("You are not allowed to register while already logged in.");
        }
        catch (FailedSessionAuthenticationException ex) {
            // this means the client doesnt have a valid sessionId
        }

        // get user model
        UserModel user = req.getBody();
        
        // validate model data
        if(user == null) throw new IllegalArgumentException("UserModel not provided.");
        if(user.getUsername() == null) throw new IllegalArgumentException("Missing username.");
        if(user.getPassword() == null) throw new IllegalArgumentException("Missing password.");
        
        
        // register user. Throws ex on failure
        userService.registerUser(user);
        
        try{
            // initiate a new session. Throws ex on failure
            res.addHeader("Set-Cookie", Auth.initiateSession(user.getUsername()));

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
    @PostMapping("/login")
    public void postLogin(@CookieValue(value="sessionId", defaultValue = "") String sessionId, HttpServletResponse res, RequestEntity<UserModel> req) {

        try{
            // if the client has a valid sessionId, update session cookie an return 401
            res.setHeader("Set-Cookie", Auth.authenticateSession(sessionId));
            throw new ForbiddenActionException("You are not allowed to log in while already logged in.");
        }
        catch (FailedSessionAuthenticationException ex) {
            // this means the client doesnt have a valid sessionId
        }

        // get model
        UserModel user = req.getBody();

        // validate model data
        if(user == null) throw new IllegalArgumentException("User not provided.");
        if(user.getUsername() == null) throw new IllegalArgumentException("Missing JSON parameter: username");
        if(user.getPassword() == null) throw new IllegalArgumentException("Missing JSON parameter: password");
        
        // authenticate user. Throws ex on failure
        Auth.authenticateUser(user.getUsername(), user.getPassword());

        // initiate a new session. Throws ex on failure
        res.addHeader("Set-Cookie", Auth.initiateSession(user.getUsername()));

        // return 200 and suggest /
        res.addHeader("Location", URL.getUrl(req, "/"));
        res.setStatus(HttpServletResponse.SC_OK);
    }


    // Handles UsernameTaken
    @ExceptionHandler(UsernameTakenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String UsernameTakenHandler(UsernameTakenException ex){
        return ex.getMessage();
    }
}
