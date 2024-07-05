package com.alfredcode.socialWebsite.service.post;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alfredcode.socialWebsite.DAO.PostDAO;
import com.alfredcode.socialWebsite.model.PostModel;

/*
 * Manages business logic for the post entities
 */
@Service
public class PostService {
    @Autowired
    private PostDAO postDao = null;


    public PostModel getPostById(Integer postId) {
        return postDao.getPostById(postId);
    }
}
