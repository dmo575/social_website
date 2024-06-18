package com.alfredcode.socialWebsite.DAO;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alfredcode.socialWebsite.Database;
import com.alfredcode.socialWebsite.Models.UserModel;
import com.alfredcode.socialWebsite.tools.SessionData;

// CRUD: CREATE, REMOVE, UPDATE, DELETE

public class UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
    public Database db = Database.getInstance();

    // CREATE - User
    public boolean createUser(UserModel u) {

        //boolean success = db.users.add(u);
        boolean success = db.createUser(u);

        if(success) {

            logger.warn("CREATE user: " + u.getUsername() + ", " + u.getPassword() + ". t="+ db.getUsersCount());
            return true;
        }

        logger.warn("CREATE user: FAIL");
        return false;
        //return db.users.add(u);
    }

    // QUERY - User or NULL if not found
    public UserModel getUserByName(String name) {

        for(UserModel u : db.getAllUsers()) {

            if(u.getUsername().equals(name)) {

                return u;
            }
        }

        return null;
    }

    // CREATE/UPDATE - Session
    public void setSession(String username, String sessionHash, Date expires) {
        
        SessionData sessionData = new SessionData(username, expires);
        db.addSession(sessionHash, sessionData);
    }

    // QUERY - Session
    public SessionData getSessionByHash(String sessionHash) {

        return db.getSessionData(sessionHash);
    }

    // DELETE - Session
    public void removeSession(String sessionHash) {
        
        db.removeSession(sessionHash);
    }
}
