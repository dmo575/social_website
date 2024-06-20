package com.alfredcode.socialWebsite.Models;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SessionModel {

    // Fields
    private Integer id = null;
    private String username = null;
    private Date expiration = null;

    // Constructors
    public SessionModel(Integer id, String username, Date expiration) {
        this.username = username;
        this.expiration = expiration;
    }
}
