package com.alfredcode.socialWebsite.DAO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alfredcode.socialWebsite.Database;
import com.alfredcode.socialWebsite.model.UserModel;


/*
 * Manages CRUD operations for the user table
 */
public class UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
    public Database db = Database.getInstance();

    public UserModel addUser(UserModel userModel) {

        try {

            Properties protperties = new Properties();
            String databaseIp = null;
            String databasePort = null;
            String databaseName = null;
            String username = null;
            String password = null;

            try{
                protperties.load(new FileInputStream("./sot"));
                databaseIp = protperties.getProperty("database.ip");
                databasePort = protperties.getProperty("database.port");
                databaseName = protperties.getProperty("database.name");
                username = protperties.getProperty("database.username");
                password = protperties.getProperty("database.password");

            }catch(IOException ex) {
                logger.error("Damn son, no file found. What you smoking san");
            }

            Connection connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s", databaseIp, databasePort, databaseName), username, password);

            PreparedStatement prepStatement = connection.prepareStatement("INSERT INTO user(username, password) VALUES(?, ?)");
            prepStatement.setString(1, userModel.getUsername());
            prepStatement.setString(2, userModel.getPassword());

            prepStatement.executeUpdate();

        }
        catch(SQLException ex) {
            logger.error("ERR ->>>>> SQL EX" + ex.getMessage());
        }
        

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
