package com.alfredcode.socialWebsite.Models;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class PostModel {

    // fields
    private Integer id = null;
    private Integer userId = null;
    private String title = null;
    private String content = null;
    private Integer views = null;
    private Date time = null;
    private String category = null;
    private String hash1 = null;
    private String hash2 = null;
    private String hash3 = null;

    // constructors
    public PostModel(){};
    public PostModel(Integer authorId, String title, String content,
    String category, String hash1, String hash2, String hash3) {
        this.userId = authorId;
        this.title = title;
        this.content = content;
        this.category = category;
        this.hash1 = hash1;
        this.hash2 = hash2;
        this.hash3 = hash3;
        this.views = 0;
        this.time = new Date();
    }

    public PostModel(Integer id, Integer authorId, String title, String content,
    String category, String hash1, String hash2, String hash3) {
        this(authorId, title, content,category,hash1, hash2, hash3);
        this.id = id;
    }
    
}
