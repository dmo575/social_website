package com.alfredcode.socialWebsite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import com.alfredcode.socialWebsite.tools.Auth;

import jakarta.servlet.http.HttpServletResponse;

/*
 * Controllers that return PAGES
 */
@Controller
public class PageController {
    

    /*
     *  GET /
     * 
     *  Serves the portal and welcome pages.
     * 
     *  200
     */
    @GetMapping("/")
    public String home(@CookieValue(value="sessionId", defaultValue="") String sessionId, HttpServletResponse res) {

        try{
            // if authenticated, 200 portal
            res.setHeader("Set-Cookie", Auth.authenticateSession(sessionId));

            return "forward:/portal.html";
        }
        catch(Exception ex) {
            
            // else, 200 welcome
            return "forward:/welcome.html";
        }
    }
}