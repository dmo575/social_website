package com.alfredcode.socialWebsite.Models;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class SessionModel {

    // Fields
    private String id = null;
    private String username = null;
    private Long expirationDateUnix = null;
    private Long refreshDateUnix = null;
    private Integer version = null;

    // Constructors
    public SessionModel(){};
    public SessionModel(String id, String username, Long expirationDateUnix, Long refreshDateUnix) {
        this.id = id;
        this.username = username;
        this.expirationDateUnix = expirationDateUnix;
        this.refreshDateUnix = refreshDateUnix;
        this.version = 0;
    }
}
