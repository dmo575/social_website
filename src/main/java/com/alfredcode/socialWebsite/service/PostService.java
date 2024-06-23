package com.alfredcode.socialWebsite.service;


import org.springframework.stereotype.Service;

import com.alfredcode.socialWebsite.DAO.PostDAO;
import com.alfredcode.socialWebsite.model.PostModel;

/*
 * Manages business logic for the post entities
 */
@Service
public class PostService {
    private PostDAO postDao = new PostDAO();


    public PostModel getPostById(Integer postId) {
        return postDao.getPostById(postId);
    }
}
