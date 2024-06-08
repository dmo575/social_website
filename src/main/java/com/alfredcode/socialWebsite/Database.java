package com.alfredcode.socialWebsite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alfredcode.socialWebsite.Models.UserModel;
import com.alfredcode.socialWebsite.Tables.UIButton;
import com.alfredcode.socialWebsite.tools.SessionData;

// mock database class, singleton
public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    private static Database database = new Database();
    public List<UserModel> users = new ArrayList<UserModel>();
    public HashMap<String, SessionData> sessions = new HashMap<>();
    public ArrayList<UIButton> button = new ArrayList<UIButton>();

    public static Database getInstance() {
        return database;
    }

    private Database() {
        init();
    }

    public void wipeData() {
        database.users.clear();
        database.sessions.clear();
    }

    // initialize all the "tables"
    public void init() {

        button.add(new UIButton("guest", "login", "null"));
        button.add(new UIButton("guest", "register", "null"));
        button.add(new UIButton("portal", "account", "null"));
        button.add(new UIButton("portal", "myspace", "null"));
        button.add(new UIButton("portal", "frontpage", "null"));
        button.add(new UIButton("portal", "contacts", "null"));
        button.add(new UIButton("post", "author", "null"));
        button.add(new UIButton("general", "back", "null"));

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
