package com.alfredcode.socialWebsite.DAO;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alfredcode.socialWebsite.Database;
import com.alfredcode.socialWebsite.model.PostModel;

/*
 * Manages CRUD operations for the post table.
 */
@Component
public class PostDAO {
    private Database db = Database.getInstance();

    @Autowired
    public DataSource ds = null;

    public PostDAO(DataSource ds) {
        this.ds = ds;
    }
    

    public PostModel getPostById(Integer postId) {
        return db.getPostById(postId);
    }
}
