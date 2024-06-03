package com.alfredcode.socialWebsite.DAO;

import java.util.Date;

import com.alfredcode.socialWebsite.Database;
import com.alfredcode.socialWebsite.Models.UserModel;
import com.alfredcode.socialWebsite.tools.SessionData;

public class UserDAO {
    public Database db = Database.getInstance();

    public boolean createUser(UserModel u) {
        return db.users.add(u);
    }

    public UserModel getUserByName(String name) {

        UserModel user = null;
        for(UserModel u : db.users) {
            if(u.getName() == name) {
                user = u;
                break;
            }
        }
        return user;
    }

    public void setSession(String username, String sessionHash, Date expires) {
        
        SessionData sessionData = new SessionData(username, expires);
        db.sessions.put(sessionHash, sessionData);
    }

    public SessionData getSessionById(String sessionId) {
        return db.sessions.get(sessionId);
    }
}
