package com.alfredcode.socialWebsite.Controllers;

import java.io.IOException;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.alfredcode.socialWebsite.Models.Check;
import com.alfredcode.socialWebsite.Models.User;
import com.alfredcode.socialWebsite.Services.UserService;

import jakarta.servlet.http.HttpServletResponse;


// Endpoints for general interactions with the API that 

@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(FrontEndController.class);
    private UserService userService = new UserService();


    @GetMapping("/check/username/{username}")
    public Check checkUsername(@PathVariable("username") String username) {

        return userService.check_usernameExists(username);
    }

    @PostMapping("/register")
    public RedirectView postRegister(RequestEntity<User> req) {
        
        User user = req.getBody();

        // add user
        user = userService.create_user(user);

        // if the user is null we send back a View, else we redirect.

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/login"));

        // redirect to /login
        // add user data
        // log them in.
        // redirect them to /home
        return new RedirectView("/login");
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User postLogin(RequestEntity<User> req) {
        
        // set cookie
        // redirect to /home
        User user = req.getBody();

        user.redactPassword();

        return null;
    }
}
