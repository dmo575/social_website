package com.alfredcode.socialWebsite.DAO;

import com.alfredcode.socialWebsite.Database;
import com.alfredcode.socialWebsite.Models.PostModel;

public class PostDAO {
    private Database db = Database.getInstance();
    
    // GET - Posts
    public PostModel[] getPosts() {
        return db.getAllPosts();
    }
}
