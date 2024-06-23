package com.alfredcode.socialWebsite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * Controllers that return VIEWS
 */
@Controller
public class ViewController {
    
    /*
     * GET /account
     * 
     * Returns the account View.
     * 
     * 200
     */
    @GetMapping("/account")
    public String getAccountView() {
        return "account";
    }
}
