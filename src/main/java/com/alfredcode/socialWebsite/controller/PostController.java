package com.alfredcode.socialWebsite.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alfredcode.socialWebsite.model.PostModel;
import com.alfredcode.socialWebsite.service.PostService;

import jakarta.websocket.server.PathParam;

/*
 * REST API for Posts
 */
@RestController
public class PostController {
    private PostService postService = new PostService();

    /*
     * POST /post/{post_id}
     * 
     * Returns a post by post ID
     * 
     * 200, 401, 404
     */
    @GetMapping("/post/{post_id}")
    public PostModel getPostById(@PathParam("post_id") Integer postId) {

        // TODO: Authentication, 401

        // 400
        if(postId == null) throw new IllegalArgumentException("Invalid post_id");

        // 200
        return postService.getPostById(postId);// << possible 404
    }

}
