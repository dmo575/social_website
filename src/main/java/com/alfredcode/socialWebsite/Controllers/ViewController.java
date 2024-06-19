package com.alfredcode.socialWebsite.Controllers;

import org.springframework.web.bind.annotation.GetMapping;

public class ViewController {
    
    @GetMapping("/account")
    public String account() {
        return "account";
    }
}
