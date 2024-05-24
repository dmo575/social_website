package com.alfredcode.socialWebsite;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Frontend {

    @GetMapping("/tryme")
    public String getIndex(){
        return "index";
    }
}
