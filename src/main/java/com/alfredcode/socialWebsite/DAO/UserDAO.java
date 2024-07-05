package com.alfredcode.socialWebsite.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alfredcode.socialWebsite.Database;
import com.alfredcode.socialWebsite.model.UserModel;


/*
 * Manages CRUD operations for the user table
 */
@Component
public class UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
    //public Database db = Database.getInstance();

    @Autowired
    public DataSource ds = null;

    public UserDAO(DataSource ds) {
        this.ds = ds;
    }

    public UserModel addUser(UserModel userModel) {
      
        try{
            Connection connection = ds.getConnection();

            PreparedStatement prepStatement = connection.prepareStatement("INSERT INTO user(username, password) VALUES(?, ?)");
            prepStatement.setString(1, userModel.getUsername());
            prepStatement.setString(2, userModel.getPassword());

            prepStatement.executeUpdate();
        }
        catch(SQLException err) {
            logger.error("Damn");
        }

        return null;//db.addUser(userModel);
    }

    public UserModel getUserByUsername(String username) {

        return null;//db.getUserByUsername(username);
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
