package com.alfredcode.socialWebsite.Controllers;

import java.security.InvalidParameterException;

import javax.management.AttributeNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.alfredcode.socialWebsite.Models.UserModel;
import com.alfredcode.socialWebsite.tools.Auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Endpoints that return HTML static pages reside here

@Controller
public class FrontEndController {

    private static final Logger logger = LoggerFactory.getLogger(FrontEndController.class);


    // register GET
    @GetMapping("/register")
    public String getRegiser(){
        return "../static/register.html";
    }

    // guest home GET
    @GetMapping("/home")
    public String index(@CookieValue(value="sessionId", defaultValue="") String sessionId, HttpServletResponse res){
        
        // if authentication passed, return page
        logger.warn("SID: " + sessionId);
        if(!sessionId.isEmpty() && Auth.authenticateUser(sessionId, res)) {
            res.setStatus(HttpServletResponse.SC_OK);
            return "../static/home.html";
        }

        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return "../static/register.html";
    }

}
