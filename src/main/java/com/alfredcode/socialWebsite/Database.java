package com.alfredcode.socialWebsite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alfredcode.socialWebsite.Models.UserModel;
import com.alfredcode.socialWebsite.tools.SessionData;

// mock database class, singleton
public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    private static Database database = new Database();
    public List<UserModel> users = new ArrayList<UserModel>();
    public HashMap<String, SessionData> sessions = new HashMap<>();

    public static Database getInstance() {
        return database;
    }

    private Database() {}

    public void wipeData() {
        database.users.clear();
        database.sessions.clear();
    }


    public UserModel getUserByName(String name) {

        for(int i = 0; i < users.size(); i++){
            UserModel cur = (UserModel) users.get(i);

            if(cur.getUsername().equals(name))
                return cur;
        }

        return null;
    }

    public boolean addSession(String sessionId, SessionData data) {
        return sessions.put(sessionId, data) != null;
    }

    public boolean removeSession(String sessionId) {
        return sessions.remove(sessionId) != null;
    }

    public SessionData getSessionData(String sessionId) {
        return sessions.get(sessionId);
    }
}
