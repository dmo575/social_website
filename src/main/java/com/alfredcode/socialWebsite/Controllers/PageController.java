package com.alfredcode.socialWebsite.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import com.alfredcode.socialWebsite.tools.Auth;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class PageController {
    

    @GetMapping("/")
    public String home(@CookieValue(value="sessionId", defaultValue="") String sessionId, HttpServletResponse res) {

        // if unauthorized, forward to welcome.html
        if(sessionId.isEmpty() || !Auth.authenticateSession(sessionId, res)) return "forward:/welcome.html";

        return "forward:/portal.html";
    }
}