package com.alfredcode.socialWebsite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alfredcode.socialWebsite.model.PostModel;
import com.alfredcode.socialWebsite.model.SessionModel;
import com.alfredcode.socialWebsite.model.UserModel;

/*
 * Mock database, singleton.
 * To be replaced with a MySQL database.
 */
public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    private static Database database = new Database();
    
    // tables
    private Map<String, SessionModel> session = new HashMap<>();
    private List<UserModel> user = new ArrayList<UserModel>();
    private List<PostModel> post = new ArrayList<PostModel>();

    

    // table id counters
    private Integer userIdCounter = 0;
    private Integer postIdCounter = 0;


    public static Database getInstance() {
        return database;
    }

    private Database() { init(); }

    public void wipeData() {
        user.clear();
        post.clear();
        session.clear();

        userIdCounter = 0;
        postIdCounter = 0;
    }

    private void init() {
        // create some posts
        addPost(new PostModel(1, "Post Title", "Hello. This is a post content", "Test", "TestingHashes", "Yep", "nullItIs"));
        addPost(new PostModel(1, "Another post title", "Hello Again. This is a post content", "TestLife", "Only", "TwoHashes", null));
    }

    
    // USER
    public UserModel addUser(UserModel userModel) {

        if(getUserByUsername(userModel.getUsername()) != null)
            return null;

        userModel.setId(++userIdCounter);

        if(user.add(userModel)) return userModel;

        --userIdCounter;
        return null;
    }

    public UserModel getUserById(Integer id) {
        
        for(UserModel u : user) {
            if(u.getId() == id) return u;
        }

        return null;
    }

    public UserModel getUserByUsername(String username) {

        for(UserModel u : user) {
            if(u.getUsername().equals(username)) return u;
        }
        return null;
    }

    public boolean removeUserById(Integer id) {
        UserModel userModel = getUserById(id);

        if(userModel != null) user.remove(userModel);

        return userModel != null;
    }

    public boolean removeUserByName(String username) {
        UserModel userModel = getUserByUsername(username);

        if(userModel != null) user.remove(userModel);

        return userModel != null;
    }


    // POST
    public boolean addPost(PostModel postModel) {
        postModel.setId(++postIdCounter);
        return post.add(postModel);
    }

    public PostModel[] getPostsByUserId(Integer userId) {
        List<PostModel> userPosts = new ArrayList<PostModel>();

        for(PostModel p : post) {
            if(p.getUserId() == userId)
                userPosts.add(p);
        }
        return userPosts.toArray(new PostModel[0]);
    }

    public PostModel getPostById(Integer postId) {
        
        for(PostModel p : post) {
            if(p.getId() == postId)
                return p;
        }

        return null;
    }


    // SESSION
    public SessionModel addSession(SessionModel sessionData) {
        session.put(sessionData.getId(), sessionData);
        return session.get(sessionData.getId());
    }

    public boolean removeSession(String sessionId, Integer sessionVersion) {
        
        if(session.get(sessionId).getVersion() == sessionVersion) {
            return session.remove(sessionId) != null;
        }

        return false;
    }

    public SessionModel getSessionById(String sessionId) {
        return session.get(sessionId);
    }

    public SessionModel getSessionByUsername(String username) {

        for(Map.Entry<String, SessionModel> entry : session.entrySet()) {
            if(entry.getValue().getUsername().equals(username))
                return entry.getValue();
        }
        return null;
    }

    public SessionModel updateSessionWithId(String sessionId, SessionModel data) {
        SessionModel s = session.get(sessionId);

        if(s!= null) return session.put(sessionId, data);

        return null;
    }

}
