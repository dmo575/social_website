package com.alfredcode.socialWebsite;

import java.io.IOException;

import org.apache.catalina.connector.Response;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletResponse;

//@RestController
@Controller
public class MyController {

    public static class Animal {
        private String name = null;
        public Animal(String n) {name = n;}
        public Animal() {name = "n";}
        public String getName() {return name;}
        public void setName(String n) {name = n;}
    }

    // HTTP - 1
    @RequestMapping(path = "/http1", method = RequestMethod.GET)
    public ResponseEntity<String> http1() {
        String str = "Hello, World! - HTTP 1";

        return ResponseEntity.ok(str);
    }

    // HTTP - 2
    @RequestMapping(path = "/http2", method = RequestMethod.GET)
    public ResponseEntity<String> http2() {
        String str = "Hello, World! - HTTP 2";

        HttpHeaders headers = new HttpHeaders();
        headers.add("my-header-http2", "my-value-http2");
        
        ResponseEntity<String> response = new ResponseEntity<String>(str, headers, HttpStatusCode.valueOf(201));
        return response;
    }

    // HTML - Servlet
    @RequestMapping(path = "/http_servlet", method = RequestMethod.GET)
    public void http_servlet(HttpServletResponse response) throws IOException {
        String str = "Hello, World! - HTTP Servlet";
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentLength(str.length());
        response.setHeader("my-header-http-servlet", "my-value-http-servlet");
        response.setContentType("text/plain");
        response.getWriter().write(str);
    }

    // REST

    @RequestMapping(path="/rest", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Animal rest() {
        Animal a = new Animal("Cow");

        return a;
    }

    // REST - Gone wrong
    @RequestMapping(path="/rest_wrong", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Animal> rest_wrong() {
        Animal a = new Animal("Cow");

        return new ResponseEntity<>(HttpStatusCode.valueOf(512));
    }

    @RequestMapping(path="/rest_wrong_2", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
	public ResponseEntity<Animal> rest_wring2() {
		try {
			Animal a = new Animal("Cow");
            //throw new Exception("ERROR");
			return new ResponseEntity<Animal>(a, HttpStatus.CREATED);
		}
		catch(Exception e) {
			return new ResponseEntity<Animal>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    // MVC - Simple
    @RequestMapping(path="/mvc_simple", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String mvc_simple(Model model) {
        Animal user = new Animal("Jeff");

        model.addAttribute("user", user);

        return "user_template";
    }

    // MVC - Complex
    @RequestMapping(path="/mvc_complex", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView mvc_complex() {
        Animal user = new Animal("Jeff the second");
        ModelAndView maw = new ModelAndView();
        maw.addObject("user", user);
        maw.setViewName("user_template");

        return maw;
    }

    // REQUEST - @RequestParam, @PathVariable
    @RequestMapping(path="req/{num}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String req(@RequestParam("query") String query, @PathVariable("num") int num) {

        return "query: " + query + ", num: " + num;
    }

    // REQUEST - @RequestBody, @RequestHeader
    @RequestMapping(path="req", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String req2(@RequestBody Animal animal, @RequestHeader("My-Header") String myHeader) {

        return animal.getName() +", " +  myHeader;
    }

    // REQUEST - RequestEntity
    @RequestMapping(path="reqEntity", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String req23(RequestEntity<Animal> req) {

        Animal a = req.getBody();
        String header = req.getHeaders().getFirst("My-Header");

        return a.getName() + ", " +header + "sdsdsdsds";
    }

}
