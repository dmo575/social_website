package com.alfredcode.socialWebsite;

import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Frontend {


    @GetMapping("/")
    public String index(RequestEntity<String> req){
        ModelAndView maw = new ModelAndView();
        HttpHeaders headers = req.getHeaders();
        String cookie = headers.getFirst("Cookie");

        maw.addObject("cookie", cookie);

        return "register";
    }


    @PostMapping("/register")
    public String register_post(RequestEntity<String> req){
        return "congrats";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }
}
