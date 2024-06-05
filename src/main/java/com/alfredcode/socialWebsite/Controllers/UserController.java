package com.alfredcode.socialWebsite.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.alfredcode.socialWebsite.Exceptions.FailedUserAuthenticationException;
import com.alfredcode.socialWebsite.Exceptions.LoginOnLoginException;
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
    

}
