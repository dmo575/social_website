package com.alfredcode.socialWebsite.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alfredcode.socialWebsite.DAO.exception.DAOException;
import com.alfredcode.socialWebsite.DAO.exception.FailureToPersistDataException;
import com.alfredcode.socialWebsite.DAO.exception.FailureToQueryDataException;
import com.alfredcode.socialWebsite.DAO.exception.FailureToRemoveDataException;
import com.alfredcode.socialWebsite.DAO.exception.FailureToUpdateDataException;
import com.alfredcode.socialWebsite.model.SessionModel;

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

    /**
     * Adds a session record to the database.
     * This method also makes sure that any other pre-existing session for the given username is deleted before adding the new one. If a user where to log in, delete cookies and log in again, this would
     * make it possible for that user to be able to log in while at the same time ensure that there is only one valid session ID per account.
     * @param sessionModel The session to add.
     * @return The session added.
     * @throws FailureToPersistDataException On failure adding session.
     */
    public SessionModel addSession(SessionModel sessionModel) throws FailureToPersistDataException, IllegalArgumentException {

        // data validation
        if(sessionModel == null || sessionModel.getId() == null || sessionModel.getUsername() == null ||
        sessionModel.getExpirationDateUnix() == null || sessionModel.getRefreshDateUnix() == null) {
            throw new IllegalArgumentException("SessionModel and its fields must not be null.");
        }

        try{
            // check if there is already a session for this user
            SessionModel existingSession = getSessionByUsername(sessionModel.getUsername());

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

            createSt.close();
            connection.close();

            // handle unexpected result case
            if(recordsAffected != 1) {
                // if no records were affected, we failed to add the record somehow.
                throw new FailureToPersistDataException("Failed to create session record: " + recordsAffected);
            }
        }
        catch(SQLException | DAOException ex) {
            logger.error("addSession::" + ex.getMessage());
            throw new FailureToPersistDataException(ex.getMessage());
        }

        return sessionModel;
    }

    /**
     * Retrieves the session for the given sessionId, if any.
     * @param sessionId The sessionId of the session record you want to retrieve.
     * @return SessionModel if found, else null.
     * @throws FailureToQueryDataException On failure querying the data.
     */
    public SessionModel getSessionById(String sessionId) throws FailureToQueryDataException, IllegalArgumentException {
        

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
            if(!rs.next()) {
                selectSt.close();
                connection.close();
                return null;
            }

            sessionModel = new SessionModel(rs.getString("id"), rs.getString("username"), rs.getLong("expiration_date_unix"), rs.getLong("refresh_date_unix"));

            selectSt.close();
            connection.close();
        }
        catch(SQLException ex) {
            logger.error("getSessionById::" + ex.getMessage());
            throw new FailureToQueryDataException(ex.getMessage());
        }

        return sessionModel;
    }

    /**
     * Retrieves the session for the given sessionId,if any.
     * @param username The username associated with the session record you want to retrieve.
     * @return SessionModel if found, else null.
     * @throws FailureToQueryDataException On failure querying the data.
     */
    public SessionModel getSessionByUsername(String username) throws FailureToQueryDataException, IllegalArgumentException {

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
            if(!rs.next()) {
                selectSt.close();
                connection.close();    
                return null;
            }

            sessionModel = new SessionModel(rs.getString("id"), rs.getString("username"), rs.getLong("expiration_date_unix"), rs.getLong("refresh_date_unix"));

            selectSt.close();
            connection.close();
        }
        catch(SQLException ex) {
            logger.error("getSessionByUsername::" + ex.getMessage());
            throw new FailureToQueryDataException(ex.getMessage());
        }

        return sessionModel;
    }

    /**
     * Attempts to update a session with the given data.
     * @param newSessionModel The new session data, including the ID of the session that you whish to update.
     * @return SessionModel on success, null if no no matching session for the given sessionId found.
     * @throws FailureToUpdateDataException On failure updating the database.
    */
    public SessionModel updateSessionById(SessionModel newSessionModel) throws FailureToUpdateDataException, IllegalArgumentException {

        // data validation
        if(newSessionModel == null || newSessionModel.getId() == null || newSessionModel.getUsername() == null ||
        newSessionModel.getExpirationDateUnix() == null || newSessionModel.getRefreshDateUnix() == null) {
            throw new IllegalArgumentException("SessionModel and its fields must not be null.");
        }

        try{
            Connection connection = ds.getConnection();

            //prepare statement
            PreparedStatement updateSt = connection.prepareStatement("UPDATE session SET username=?, expiration_date_unix=?, refresh_date_unix=? WHERE id=?");
            updateSt.setString(1, newSessionModel.getUsername());
            updateSt.setLong(2, newSessionModel.getExpirationDateUnix());
            updateSt.setLong(3, newSessionModel.getRefreshDateUnix());
            updateSt.setString(4, newSessionModel.getId());

            // execute query
            int rowsAffected = updateSt.executeUpdate();

            updateSt.close();
            connection.close();

            // handle unexpected result case:
            // 0 means that there was no previous session under the same id.
            if(rowsAffected == 0) return null;
        }
        catch(SQLException ex) {
            logger.error("updateSessionById::" + ex.getMessage(), ex);
            throw new FailureToUpdateDataException(ex.getMessage());
        }

        return newSessionModel;
    }

    /**
     * Removes a session by ID, if any.
     * @param sessionId The session Id of the session you whish to remove.
     * @return True if at least a session was removed, False if no sessions were affected.
     * @throws FailureToRemoveDataException On failure removing the record.
     */
    public Boolean removeSessionWithId(String sessionId) throws FailureToRemoveDataException, IllegalArgumentException {

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
            connection.close();
        }
        catch(SQLException ex) {
            logger.error("removeSessionWithId::" + ex.getMessage());
            throw new FailureToRemoveDataException(ex.getMessage());
        }
        
        return recordsAffected > 0;
    }

    /*
     * Every 1h, sends a delete query for all expired sessions.
     * This will run in its own thread, so race conditions apply.
     * 
     * The authentication process is organized in a way that allows us to have two threads handle the sessions table at the same time without
     * them stepping ontoeachother's feet:
     * 
     * - **Step 1: Client sends HTTP request.**
     * - **Step 2: Authentication (Auth's AOP methods)**:
     *     - Given the sessionId:
     *         - **(A)** If the session exists in the database and it is not expired, allow the request to move to the next step.
     *         - **(B)** If the session doesn't exist in the database or it is expired, deny access.
     * 
     * - **Step 3: SessionInterceptor's preHandle method**:
     *     - Given the sessionId:
     *         - **(C)** If the session exists in the database (whether or not is expired), **attempt** to update it: We allow the request to
     *                   move to the next step on success, deny access on failure.
     *         - **(D)** If the session doesn't exist in the database anymore, deny access.
     * 
     * - **Step 4: The HTTP request gets processed.**
     * 
     * Those steps are carried over on that order in the main thread.
     * 
     * The second thread runs a DELETE query on the session table every X time. A couple of things to notie about that:
     * - MySQL may receive orders at the same time but it won't execute them at the same time, at least not orders that modify a specific record. Which
     *   means that the DELETE query might run just before or just after any of the main thread queries that modify the record.
     * - When we run the DELETE query, that query has a conditional, only expired queries will get deleted.
     * 
     * With that in mind, we consider these cases:
     * 
     * - **Case 1 - session deleted/expired before Step 2**: At step 2, the client contains the sessionId of a record that has been deleted due to it
     *              being expired. Auth's AOP method will try to retrieve the session and will fail, denying access.
     *  
     * - **Case 2 - session deleted/expired right after Step 2**: The session passes authentication (Step 2), but expires/removed shortly after. The
     *              SessionInterceptor's preHandle method (Step 3) then tries to again access the session. It find the session to be expired/deleted, so
     *              it denies access.
     * 
     * - **Case 3 - session deleted before updating it**: The SessionInterceptor's preHandle method retrieves the session and it is not expired. It
     *              then prepares the new updated values for the session and sends out the update query. However, when running the update query, MySQL
     *              finds that the exception no longer exists due to the second thread deleting it just a moment ago. The update fails, and the access
     *              is denied. The reason this is possible is because we can check if an update affected any row at all, and conclude based on that.
     * 
     * - **Case 4 - A delete query arrives just after updating the query**: No deletion will happen, because that deletion has a conditional within it
     *              that will fail if the session just got updated.
     * 
     * Because we make sure to authenticate and update the session before processing the HTTP request, we can allow ourselves to let a session pass
     * authentication and expire before we get a chance to update the session.
     * 
     */
    /**
     * Periodically removes any expired sessions from the database.
     */
    //@Scheduled(fixedRate = 1000*60*60)// 1h
    @Scheduled(fixedRate = 1000*60*10)// 10m
    //@Scheduled(fixedRate = 1000*10)// 10s
    private void removeExpiredSessions() {

        try{
            Connection connection = ds.getConnection();

            PreparedStatement st = connection.prepareStatement("DELETE FROM session WHERE expiration_date_unix<?");

            st.setLong(1, (new Date()).getTime());

            int affectedRows = st.executeUpdate();

            logger.info("Removing expired sessions ("+affectedRows+")");

            st.close();
            connection.close();
        }
        catch(SQLException ex) {
            // in this case we log the error. We won't be handling this at any upper layer so no need to bubble it up.
            logger.error("removeExpiredSessions::" + ex.getMessage());
        }
    }
}
