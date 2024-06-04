package com.alfredcode.socialWebsite.Models;

public class UserModel {
    private String username = null;
    private String password = null;

    public UserModel() {}
    public UserModel(String name, String password) {
        this.username = name;
        this.password = password;
    };

    public String getUsername(){ return username; }
    public String getPassword(){ return password; }

    public void setUsername(String n) {username = n;}
    public void setPassword(String p) {password = p;}
}
