package com.alfredcode.socialWebsite.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alfredcode.socialWebsite.DAO.exception.FailureToPersistDataException;
import com.alfredcode.socialWebsite.model.UserModel;


/*
 * Manages CRUD operations for the user table
 */
@Component
public class UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    @Autowired
    public DataSource ds = null;

    public UserDAO(DataSource ds) {
        this.ds = ds;
    }

    /*
     * Persists user on the database. Returns user back (with ID)
     */
    public UserModel addUser(UserModel userModel) {

        // data validation
        if(userModel == null || userModel.getUsername() == null || userModel.getPassword() == null) {
            throw new IllegalArgumentException("userModel and its data cannot be null.");
        }
      
        try{
            Connection connection = ds.getConnection();

            // prepare query
            PreparedStatement updateSt = connection.prepareStatement("INSERT INTO user(username, password) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
            updateSt.setString(1, userModel.getUsername());
            updateSt.setString(2, userModel.getPassword());
            
            // execute query
            int updateCount = updateSt.executeUpdate();

            // handle unexpected result case
            if(updateCount != 1) {
                throw new FailureToPersistDataException("Failed to persist new user");
            }

            // get the id
            ResultSet keys = updateSt.getGeneratedKeys();
            keys.next();
            userModel.setId(keys.getInt(1));

            updateSt.close();
            connection.close();
        }
        catch(SQLException err) {
            logger.error("addUser::" + err.getMessage());
        }

        return userModel;
    }

    /*
     * Given a username, returns the user.
     */
    public UserModel getUserByUsername(String username) {

        // data validation
        if(username == null) { throw new IllegalArgumentException("username cannot be null.");}

        UserModel userModel = null;

        try{
            Connection connection = ds.getConnection();

            // prepare query
            PreparedStatement selectSt = connection.prepareStatement("SELECT * FROM user WHERE username=?");
            selectSt.setString(1, username);

            // execute query
            ResultSet rs = selectSt.executeQuery();

            // handle unexpected return case
            if(!rs.next()) return null;

            userModel = new UserModel(rs.getInt("id"), rs.getString("username"), rs.getString("password"));

            selectSt.close();
            connection.close();
        }
        catch(SQLException err) {
            logger.error("getUserByUsername::" + err.getMessage());
        }

        return userModel;
    }
}
