package com.alfredcode.socialWebsite.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class SessionModel {

    // fields
    private String id = null;
    private String username = null;
    private Long expirationDateUnix = null;
    private Long refreshDateUnix = null;
    private Integer version = null;

    // constructors
    public SessionModel(){};
    public SessionModel(String id, String username, Long expirationDateUnix, Long refreshDateUnix) {
        this.id = id;
        this.username = username;
        this.expirationDateUnix = expirationDateUnix;
        this.refreshDateUnix = refreshDateUnix;
        this.version = 0;
    }
}
