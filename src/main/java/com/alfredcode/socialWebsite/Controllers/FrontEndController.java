package com.alfredcode.socialWebsite.Controllers;


import java.security.InvalidParameterException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import com.alfredcode.socialWebsite.Exceptions.FailedSessionAuthenticationException;
import com.alfredcode.socialWebsite.tools.Auth;

import jakarta.servlet.http.HttpServletResponse;

// Endpoints that return HTML static pages

@Controller
public class FrontEndController {

    private static final Logger logger = LoggerFactory.getLogger(FrontEndController.class);
    public static final String pageHome = "../static/home.html";
    public static final String pageRegister = "../static/register.html";



    // registration page
    @GetMapping("/register")
    public String getRegiser(@CookieValue(value="sessionId", defaultValue="") String sessionId, HttpServletResponse res){

        // if authentication succeeds, then redirect to /home
        if(Auth.authenticateSession(sessionId, res)) return pageHome;

        return pageRegister;
    }

    // home page
    @GetMapping("/home")
    public String index(@CookieValue(value="sessionId", defaultValue="") String sessionId, HttpServletResponse res){

        // data validation
        if(sessionId.isEmpty()) throw new FailedSessionAuthenticationException("SessionId not found.");

        // session authentication (this takes care of redirecting if it fails)
        Auth.authenticateSession(sessionId, res);

        return pageHome;
    }

}
