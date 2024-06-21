package com.alfredcode.socialWebsite.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import com.alfredcode.socialWebsite.tools.Auth;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class PageController {
    

    /*
     *  GET - /
     *  Serves the portal and welcome pages.
     *  Status Codes:
     *  - 200
     */
    @GetMapping("/")
    public String home(@CookieValue(value="sessionId", defaultValue="") String sessionId, HttpServletResponse res) {

        try{
            // if authenticated, portal.html
            res.setHeader("Set-Cookie", Auth.authenticateSession(sessionId));

            return "forward:/portal.html";
        }
        catch(Exception ex) {
            
            // else, welcome.html
            return "forward:/welcome.html";
        }
    }
}