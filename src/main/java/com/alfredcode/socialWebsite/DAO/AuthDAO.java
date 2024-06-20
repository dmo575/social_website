package com.alfredcode.socialWebsite.DAO;

import com.alfredcode.socialWebsite.Database;
import com.alfredcode.socialWebsite.Models.SessionModel;

public class AuthDAO {
    Database db = Database.getInstance();
    
    public SessionModel addSession(String username, SessionModel sessionData) {
        return db.addSession(username, sessionData);
    }

    public SessionModel getSessionById(String sessionId) {
        return db.getSessionData(sessionId);
    }

    public SessionModel setSession(String sessionId, SessionModel sessionData) {
        return db.setSessionData(sessionId, sessionData);
    }

    public Boolean removeSession(String sessionId) {
        return db.removeSession(sessionId);
    }

}
