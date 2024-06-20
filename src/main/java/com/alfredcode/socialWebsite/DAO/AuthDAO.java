package com.alfredcode.socialWebsite.DAO;

import com.alfredcode.socialWebsite.Database;
import com.alfredcode.socialWebsite.tools.SessionData;

public class AuthDAO {
    Database db = Database.getInstance();
    
    public SessionData addSession(String username, SessionData sessionData) {
        return db.addSession(username, sessionData);
    }

    public SessionData getSessionById(String sessionId) {
        return db.getSessionData(sessionId);
    }

    public SessionData setSession(String sessionId, SessionData sessionData) {
        return db.setSessionData(sessionId, sessionData);
    }

    public Boolean removeSession(String sessionId) {
        return db.removeSession(sessionId);
    }

}
