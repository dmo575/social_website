package com.alfredcode.socialWebsite.Services;


import org.springframework.stereotype.Service;

import com.alfredcode.socialWebsite.DAO.PostDAO;
import com.alfredcode.socialWebsite.Models.PostModel;

@Service
public class PostService {
    private PostDAO postDao = new PostDAO();


    public PostModel getPostById(Integer postId) {
        return postDao.getPostById(postId);
    }
}
