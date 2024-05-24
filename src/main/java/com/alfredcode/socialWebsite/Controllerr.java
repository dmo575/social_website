package com.alfredcode.socialWebsite;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controllerr {
    
    @GetMapping("/")
    public String index(){
        return "Hi";
    }
}
