package com.alfredcode.socialWebsite.tools;

import java.util.Date;

public class SessionData {
    private String username = null;
    private Date expiration = null;
    public SessionData(String username, Date expiration) {
        this.username = username;
        this.expiration = expiration;
    }

    public Date getExpiration() { return expiration; }
    public String getUsername() { return username; }
    public void setExpiration(Date newExpiration) { expiration = newExpiration; }
}
