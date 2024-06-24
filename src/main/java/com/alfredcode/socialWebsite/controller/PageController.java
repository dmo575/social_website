package com.alfredcode.socialWebsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alfredcode.socialWebsite.security.Auth;
import com.alfredcode.socialWebsite.security.annotation.NoSessionAllowed;
import com.alfredcode.socialWebsite.service.session.SessionService;

import jakarta.servlet.http.HttpServletResponse;

/*
 * Controllers that return PAGES
 */
@Controller
public class PageController {
    
    private SessionService sessionService = null;

    @Autowired
    public PageController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

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
            res.setHeader("Set-Cookie", sessionService.updateSession(sessionId));

            return "forward:/portal.html";
        }
        catch(Exception ex) {
            
            // else, 200 welcome
            return "forward:/welcome.html";
        }
    }
}