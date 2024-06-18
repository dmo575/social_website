package com.alfredcode.socialWebsite.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alfredcode.socialWebsite.Models.PostModel;
import com.alfredcode.socialWebsite.Services.PostService;

@RestController
public class PostController {
    private PostService postService = new PostService();

    @GetMapping("/posts")
    public PostModel[] getPosts() {
        return postService.getAllPosts();
    }

}
