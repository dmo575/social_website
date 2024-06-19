package com.alfredcode.socialWebsite.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    
    @GetMapping("/account")
    public String account() {
        return "account";
    }
}
