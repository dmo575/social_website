package com.alfredcode.socialWebsite.Services;


import com.alfredcode.socialWebsite.DAO.PostDAO;
import com.alfredcode.socialWebsite.Models.PostModel;

public class PostService {
    private PostDAO postDao = new PostDAO();

    public PostModel[] getAllPosts() {
        return postDao.getPosts();
    }

    public PostModel getPostById(Integer postId) {
        return postDao.getPostById(postId);
    }
}
