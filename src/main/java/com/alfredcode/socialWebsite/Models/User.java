package com.alfredcode.socialWebsite.Models;

public class User {
    private String name = null;
    private String password = null;

    public User() {}
    public User(String name) {
        this.name = name;
    };

    public String getName(){ return name; }
    public String getPassword(){ return password; }
    public void redactPassword(){ password = "redacted"; };

    public void setName(String n) {name = n;}
    public void setPassword(String p) {password = p;}
}
