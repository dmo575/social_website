package com.alfredcode.socialWebsite.Models;

public class UserModel {
    private String name = null;
    private String password = null;

    public UserModel() {}
    public UserModel(String name, String password) {
        this.name = name;
        this.password = password;
    };

    public String getName(){ return name; }
    public String getPassword(){ return password; }

    public void setName(String n) {name = n;}
    public void setPassword(String p) {password = p;}
}
