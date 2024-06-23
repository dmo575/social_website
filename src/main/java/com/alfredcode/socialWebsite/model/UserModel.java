package com.alfredcode.socialWebsite.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class UserModel {

    // fields
    private Integer id = null;
    private String username = null;
    private String password = null;

    // constructors
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
