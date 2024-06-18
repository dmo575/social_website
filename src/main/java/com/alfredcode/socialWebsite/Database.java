package com.alfredcode.socialWebsite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alfredcode.socialWebsite.Models.PostModel;
import com.alfredcode.socialWebsite.Models.UserModel;
import com.alfredcode.socialWebsite.Services.UserService;
import com.alfredcode.socialWebsite.tools.SessionData;

// mock database class, singleton
public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    private static Database database = new Database();
    
    // tables
    private HashMap<String, SessionData> sessions = new HashMap<>();
    private List<UserModel> users = new ArrayList<UserModel>();
    private List<PostModel> posts = new ArrayList<PostModel>();

    // table id counters
    private Integer usersCount = 0;
    private Integer postsCount = 0;


    public static Database getInstance() {
        return database;
    }

    private Database() { init(); }

    public void wipeData() {
        database.users.clear();
        database.posts.clear();
        database.sessions.clear();

        usersCount = 0;
        postsCount = 0;
    }

    private void init() {
        // create some posts
        createPost(new PostModel(1, "Post Title", "Hello. This is a post content", "Test", "TestingHashes", "Yep", "nullItIs"));
        createPost(new PostModel(1, "Another post title", "Hello Again. This is a post content", "TestLife", "Only", "TwoHashes", null));
    }

    // CRUD USERS   ==  ==  ==  ==
    public boolean createUser(UserModel user) {
        user.setId(++usersCount);
        return users.add(user);
    }

    public UserModel getUserById(Integer id) {
        
        for(UserModel u : users) {
            if(u.getId() == id) return u;
        }

        return null;
    }

    public UserModel getUserByUsername(String username) {

        for(UserModel u : users) {
            if(u.getUsername().equals(username)) return u;
        }
        return null;
    }

    public UserModel[] getAllUsers() {
        return users.toArray(new UserModel[0]);
    }

    public Integer getUsersCount() {
        return users.size();
    }

    public boolean deleteUserById(Integer id) {
        UserModel model = getUserById(id);
        return users.remove(model);
    }

    public boolean deleteUserByName(String username) {
        UserModel model = getUserByUsername(username);
        return users.remove(model);
    }
    // END CRUD USERS   ==  ==  ==


    // CRUD POSTS   ==  ==  ==  ==
    public boolean createPost(PostModel post) {
        post.setId(++postsCount);
        return posts.add(post);
    }

    public PostModel[] getPostsByUserId(Integer userId) {
        List<PostModel> userPosts = new ArrayList<PostModel>();

        for(PostModel p : posts) {
            if(p.getUserId() == userId)
                userPosts.add(p);
        }
        return userPosts.toArray(new PostModel[0]);
    }

    public PostModel[] getAllPosts() {
        return posts.toArray(new PostModel[0]);
    }
    // END CRUD POSTS   ==  ==  ==


    // CRUD SESSIONS   ==  ==  ==  ==
    public boolean addSession(String sessionHash, SessionData sessionData) {
        return sessions.put(sessionHash, sessionData) != null;
    }

    public boolean removeSession(String sessionHash) {
        return sessions.remove(sessionHash) != null;
    }

    public SessionData getSessionData(String sessionId) {
        return sessions.get(sessionId);
    }
    // END CRUD SESSIONS   ==  ==  ==

}
