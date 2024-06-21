package com.alfredcode.socialWebsite.Models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class UserModel {
    private Integer id = null;
    private String username = null;
    private String password = null;

    public UserModel() {}
    public UserModel(String name, String password) {
        this.username = name;
        this.password = password;
    };
    public UserModel(Integer id, String name, String password) {
        this(name, password);
        this.id = id;
    }
}
