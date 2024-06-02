package com.alfredcode.socialWebsite.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Endpoints that return HTML static pages reside here

@Controller
public class FrontEndController {

    private static final Logger logger = LoggerFactory.getLogger(FrontEndController.class);

    // guest home GET
    @GetMapping("/")
    public String index(){
        return "../static/index.html";
    }

    // login GET
    @GetMapping("/login")
    public String getLogin(){
        return "../static/login.html";
    }

    // register GET
    @GetMapping("/register")
    public String getRegiser(){
        return "../static/register.html";
    }



}
