package com.alfredcode.socialWebsite;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alfredcode.socialWebsite.Controllers.FrontEndController;
import com.alfredcode.socialWebsite.Models.User;

public class Database {
    private static final Logger logger = LoggerFactory.getLogger(FrontEndController.class);

    private static Database database = new Database();
    public List<User> users = new ArrayList<User>();

    public static Database getInstance() {
        return database;
    }

    public User getUserByName(String name) {

        for(int i = 0; i < users.size(); i++){
            User cur = (User) users.get(i);

            if(cur.getName().equals(name))
                return cur;
        }

        return null;
    }
}
