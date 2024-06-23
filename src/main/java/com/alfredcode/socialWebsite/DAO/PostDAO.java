package com.alfredcode.socialWebsite.DAO;

import com.alfredcode.socialWebsite.Database;
import com.alfredcode.socialWebsite.model.PostModel;

/*
 * Manages CRUD operations for the post table.
 */
public class PostDAO {
    private Database db = Database.getInstance();
    

    public PostModel getPostById(Integer postId) {
        return db.getPostById(postId);
    }
}
