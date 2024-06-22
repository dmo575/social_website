package com.alfredcode.socialWebsite.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alfredcode.socialWebsite.Database;
import com.alfredcode.socialWebsite.Models.UserModel;


/*
 * Manages CRUD operations for the user table
 */
public class UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
    public Database db = Database.getInstance();

    public UserModel addUser(UserModel userModel) {

        return db.addUser(userModel);
    }

    public UserModel getUserByUsername(String username) {

        return db.getUserByUsername(username);
    }

    /* public void setSession(String username, String sessionHash, Date expires) {
        
        SessionData sessionData = new SessionData(username, expires);
        db.addSession(sessionHash, sessionData);
    }

    public SessionData getSessionByHash(String sessionHash) {

        return db.getSessionData(sessionHash);
    }

    public void removeSession(String sessionHash) {
        
        db.removeSession(sessionHash);
    } */
}
