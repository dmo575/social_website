package com.alfredcode.socialWebsite.Controllers;

import java.security.InvalidParameterException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.alfredcode.socialWebsite.Exceptions.UserRegistrationException;
import com.alfredcode.socialWebsite.Exceptions.UsernameTakenException;
import com.alfredcode.socialWebsite.Models.UserModel;
import com.alfredcode.socialWebsite.Services.UserService;
import com.alfredcode.socialWebsite.tools.Auth;

import jakarta.servlet.http.HttpServletResponse;

// Endpoints focused on managing User data

@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserService userService = new UserService();


    // registers a user and opens a session
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserModel postRegister(HttpServletResponse res, RequestEntity<UserModel> req) {
       
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

        return user;
    }

    @ExceptionHandler(UsernameTakenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String UsernameTakenHandler(UsernameTakenException ex){
        return ex.getMessage();
    }


    @ExceptionHandler(UserRegistrationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String userRegistrationHandler(UserRegistrationException ex){
        return ex.getMessage();
    }

}
