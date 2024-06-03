package com.alfredcode.socialWebsite.Controllers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;
import java.nio.file.AccessDeniedException;
import java.security.InvalidParameterException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.alfredcode.socialWebsite.Database;
import com.alfredcode.socialWebsite.Exceptions.AuthenticationFailedException;
import com.alfredcode.socialWebsite.Exceptions.UserRegistrationException;
import com.alfredcode.socialWebsite.Exceptions.UsernameTakenException;
import com.alfredcode.socialWebsite.Models.UserModel;
import com.alfredcode.socialWebsite.Services.UserService;
import com.alfredcode.socialWebsite.tools.Auth;

import jakarta.servlet.http.HttpServletResponse;

// Endpoints for general interactions with the API that 

@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserService userService = new UserService();


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserModel postRegister(HttpServletResponse res, RequestEntity<UserModel> req) {
        
        UserModel user = req.getBody();
        
        // register user
        if(user == null) { throw new InvalidParameterException("User not provided."); }

        userService.registerUser(user);

        // create session
        Auth.setSession(user.getName(), res);

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
