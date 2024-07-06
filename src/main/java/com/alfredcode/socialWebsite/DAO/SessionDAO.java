package com.alfredcode.socialWebsite.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alfredcode.socialWebsite.DAO.exception.FailureToPersistDataException;
import com.alfredcode.socialWebsite.model.SessionModel;

/*
 * Avoiding race conditions:
 * The authentication process is organized in a way that allows me to have two threads handle the sessions table
 * at the same time without any of them stepping onto the other's feet:
 * 
 * A- Client sends HTTP request
 * 
 * B- Auth's AOP method kicks in:
 * B1-- Given the sessionId:
 * B1.1--- if the session exists in the database and its not expired, pass
 * B1.2--- if the session doesn't exist in the database or it is expired, deny access
 * 
 * C- SessionInterceptor's preHandle method kicks in:
 * C1-- Given the sessionId:
 * C1.1--- If the session exists in the database (whether or not it is expired), attempt to update it.
 * C1.1.1----- If the update failed (the session might not exist or the database might have failed), deny access.
 * C1.2--- If the session doesn't exist in the database anymore, deny access
 * 
 * D- The HTTP request gets processed
 * 
 * All that happens in that order on the main thread.
 * 
 * The second thread runs a DELETE query on the session table every X time. We know this:
 * - MySQL may receive orders at the same time but it won't execute them at the same time, at least orders that modify
 * a specific record. Which means that the DELETE query might run just before or just after any of the main thread queries.
 * - When we run the DELETE query, that query has a conditional, only expired queries will get deleted.
 * 
 * These are the danger cases, which are being handled just fine due to the order of things:
 * 
 * Case 1 (pre B): The client sends the sessionId of a session record that has been deleted due to it being expired. Auth's AOP
 * method will try to retrieve the session and will fail, denying access.
 * 
 * Case 2 (pre C): The session passes authentication, but expires shortly after. The SessionInterceptor's preHandle method
 * then tries to again access the session. It finds it but it is expired, or maybe it doesn't find it
 * at all. For both cases the outcome is the same: access denied.
 * 
 * Case 3 (pre C1.1.1): The SessionInterceptor's preHandle method retrieves the session and it is not expired. It then
 * locally updates it and sends an update query. However, when running the update query, MySQL finds that the exception
 * no longer exists due to the second thread deleting it just a moment ago. The update fails, and the access is denied.
 * The reason this is possible is because we can check if an update affected any row at all, and conclude based on that.
 * 
 * Because we make sure to authenticate and update the session before processing the HTTP request, we can allow ourselves
 * to let a session pass authentication and expire before we get a chance to update the session.
 * 
 */

 /*
  * Manages CRUD operations for the session table
  */
@Component
public class SessionDAO {
    private static final Logger logger = LoggerFactory.getLogger(SessionDAO.class);

    @Autowired
    public DataSource ds = null;

    public SessionDAO(DataSource ds) {
        this.ds = ds;
    }

    /*
     * Adds the session to the database.
     * Makes sure there are no other extra sessions for the same user before adding it.
     */
    public SessionModel addSession(SessionModel sessionModel) {


        // data validation
        if(sessionModel == null || sessionModel.getId() == null || sessionModel.getUsername() == null ||
        sessionModel.getExpirationDateUnix() == null || sessionModel.getRefreshDateUnix() == null) {
            throw new IllegalArgumentException("SessionModel and its fields must not be null.");
        }


        // check if there is already a session for this user
        SessionModel existingSession = getSessionByUsername(sessionModel.getUsername());

        try{
            if(existingSession != null) {
                removeSessionWithId(existingSession.getId());        
            }

            Connection connection = ds.getConnection();

            // prepare statement
            PreparedStatement createSt = connection.prepareStatement("INSERT INTO session(id, username, expiration_date_unix, refresh_date_unix) VALUES(?, ?, ?, ?)");
            createSt.setString(1, sessionModel.getId());
            createSt.setString(2, sessionModel.getUsername());
            createSt.setLong(3, sessionModel.getExpirationDateUnix());
            createSt.setLong(4, sessionModel.getRefreshDateUnix());

            // execute query
            int recordsAffected = createSt.executeUpdate();

            // handle unexpected result case
            if(recordsAffected != 1) {
                throw new FailureToPersistDataException("Failed to create session record: " + recordsAffected);
            }
        }
        catch(SQLException err) {
            logger.error("addSession::" + err.getMessage());
        }

        return sessionModel;
    }

    /*
     * returns the session of the given sessionId
     */
    public SessionModel getSessionById(String sessionId) {
        

        // data validation
        if(sessionId == null) throw new IllegalArgumentException("sessionId must not be null.");

        SessionModel sessionModel = null;

        try{
            Connection connection = ds.getConnection();

            // prepare query
            PreparedStatement selectSt = connection.prepareStatement("SELECT * FROM session WHERE id=?");
            selectSt.setString(1, sessionId);

            // execute query
            ResultSet rs = selectSt.executeQuery();

            // handle unexpected result case
            if(!rs.next()) return null;

            sessionModel = new SessionModel(rs.getString("id"), rs.getString("username"), rs.getLong("expiration_date_unix"), rs.getLong("refresh_date_unix"));

            selectSt.close();
        }
        catch(SQLException err) {
            logger.error("getSessionById::" + err.getMessage());
        }

        return sessionModel;
    }

    /*
     * returns the session of the given username
     */
    public SessionModel getSessionByUsername(String username) {

        // data validation
        if(username == null) throw new IllegalArgumentException("username must not be null.");

        SessionModel sessionModel = null;

        try{
            Connection connection = ds.getConnection();

            // prepare query
            PreparedStatement selectSt = connection.prepareStatement("SELECT * FROM session WHERE username=?");
            selectSt.setString(1, username);

            // execute query
            ResultSet rs = selectSt.executeQuery();

            // handle unexpected result case
            if(!rs.next()) return null;

            sessionModel = new SessionModel(rs.getString("id"), rs.getString("username"), rs.getLong("expiration_date_unix"), rs.getLong("refresh_date_unix"));

            selectSt.close();
        }
        catch(SQLException err) {
            logger.error("getSessionByUsername::" + err.getMessage());
        }

        return sessionModel;
    }

    /*
     * Attempts to update the session.
     */
    public SessionModel updateSession(SessionModel sessionModel) {

        // data validation
        if(sessionModel == null || sessionModel.getId() == null || sessionModel.getUsername() == null ||
        sessionModel.getExpirationDateUnix() == null || sessionModel.getRefreshDateUnix() == null) {
            throw new IllegalArgumentException("SessionModel and its fields must not be null.");
        }

        try{
            Connection connection = ds.getConnection();

            //prepare statement
            PreparedStatement updateSt = connection.prepareStatement("UPDATE session SET username=?, expiration_date_unix=?, refresh_date_unix=?, version=? WHERE id=?");
            updateSt.setString(1, sessionModel.getUsername());
            updateSt.setLong(2, sessionModel.getExpirationDateUnix());
            updateSt.setLong(3, sessionModel.getRefreshDateUnix());

            // execute query
            ResultSet rs = updateSt.executeQuery();

            // handle unexpected result case
            if(!rs.next()) return null;

            sessionModel = new SessionModel(rs.getString("id"), rs.getString("username"), rs.getLong("expiration_date_unix"), rs.getLong("refresh_date_unix"));

            updateSt.close();
        }
        catch(SQLException err) {
            logger.error("updateSession::" + err.getMessage());
        }

        return sessionModel;
    }

    public Boolean removeSessionWithId(String sessionId) {

        // data validation
        if(sessionId == null) throw new IllegalArgumentException("sessionId must not be null.");

        int recordsAffected = -1;

        try{
            Connection connection = ds.getConnection();

            // prepare query
            PreparedStatement removeSt = connection.prepareStatement("DELETE FROM session WHERE id=?");
            removeSt.setString(1, sessionId);

            // execute query
            recordsAffected = removeSt.executeUpdate();

            removeSt.close();

        }
        catch(SQLException err) {
            logger.error("removeSessionWithId::" + err.getMessage());
        }
        
        return recordsAffected > 0;
    }
}
