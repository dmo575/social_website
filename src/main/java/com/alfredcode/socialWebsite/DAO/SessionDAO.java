package com.alfredcode.socialWebsite.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alfredcode.socialWebsite.Database;
import com.alfredcode.socialWebsite.Models.SessionModel;

/*
 * Race conditions:
 * 
 *  Because we have two threads that manage the same session table, we need to ensure that they dont step on each other's feet.
 * - Thread 1 (main one, Auth.authenticateSession): Checks if a session has expired. If not, it updates it accordingly.
 * - Thread 2 (Auth's block scope): Check if a session has expired. If so, it deletes it from the database.
 * 
 * - Case 1, problem:
 *      Thread 1 checks the session and sees it hasnt expired.
 *      Thread 2 then checks the session and sees it has expired.
 *      Thread 2 deletes the session.
 *      Thread 1 prepares the update for the session and sends it, but the session has already been deleted by Thread 2.
 * - Case 1, solution:
 *      SQLite wont throw any errors if you attempt to modify a non existent field (update var where user = 5, but theres no user 5)
 *      You can however, check how many rows were affected. So if none were affected, it means the session had been deleted by that time.
 * 
 * - Case 2, problem:
 *      Thread 2 reads a session and finds it has expired.
 *      Thread 1 updates that session.
 *      Thread 2 then proceeds to remove the session.
 *      Now we have Thread 1 thinking all is fine with the session when it no longer exists.
 * - Case 2, solution:
 *      We can implement "Optimistic Locking" to solve this.
 *      Optimistic Locking consists on first adding a version number column to our table.
 *      We then make sure that we know the version number of the row we want to modify, meaning we add it to our WHERE condition.
 *      We also make sure to update that version number (increment it by 1) each time we modify the record.
 *      We can only know the version number by first reading it so we need to read the record first.
 *      This is how this plays out with Threads 1 and 2:
 *          -Thread 1 reads a session. It finds out that the session is NOT expired and that the session's version is 3.
 *          -Thread 2 reads the same session. It finds out that the session IS expired and that the sesion's version is 3.
 *          -Thread 1 sends an update to the session where it updates the session expiration and increments the version number by 1, like this:
 *              UPDATE session WHERE id=xsff45tt AND version=3
 *          -Since the session's version is 3, the update completes. We sucessfully check that it did so by checking the afected rows count.
 *          -Thread 2 sends a delete query to the session, like this:
 *              DELETE session WHERE id=xsff45tt AND version=3
 *          -The session id is correct, but the version is no longer 3, so the changes don't apply and we confirm that by checking the affected rows count (0).
 * 
 */

 /*
  * Manages CRUD operations for the session table
  */
public class SessionDAO {
    private static final Logger logger = LoggerFactory.getLogger(SessionDAO.class);
    Database db = Database.getInstance();

    /*
     * Adds the session to the database.
     * Makes sure there are no other extra sessions for the same user before adding it.
     */
    public SessionModel addSession(SessionModel sessionModel) {

        // check if there is already a session for this user
        SessionModel currSession = getSessionByUsername(sessionModel.getUsername());

        // if there is not, add one and return it
        if(currSession == null) return db.addSession(sessionModel);

        // if there is one, remove it
        // (if a user logged in, cleared his cookies and logged in again, this could be true)
        removeSessionWithId(currSession.getId());

        // question: What if a thread has it to remove this currSession? In theory it should not matter because the sessionID is different.
        // so even if we create this new session for the same user, the ID is different so the other thread will not find it since it deleted by session ID
        // but in the impossible case that for some reason the session ID ends up being the same (because even tho its random, it can be the same twice)
        // we would need to tackle that... we could add the creation time to the session ID generation...

        // add session and return it
        return db.addSession(sessionModel);
    }

    /*
     * returns the session of the given sessionId
     */
    public SessionModel getSessionById(String sessionId) {
        return db.getSessionById(sessionId);
    }

    /*
     * returns the session of the given username
     */
    public SessionModel getSessionByUsername(String username) {
        return db.getSessionByUsername(username);
    }

    /*
     * Looks to update the SessionModel.
     * 
     * forceUpdate: Means that even if the version changed (Optimistic Lock), we still want to push the update. For our project we want to do so. Our only concern
     * is when deleting sessions, since there is only one Thread that will be pushing updates and another one that will be focusing on deleting expired
     * records. This would probably be different if more servers join. We would probably have to look at transactions and locking levels within the database records.
     * 
     */
    public SessionModel updateSessionWithId(String sessionId, SessionModel sessionModel, Boolean forceUpdate) {

        // get session
        SessionModel currSession = getSessionById(sessionId);

        // if we cannot find the session, it means the other thread removed it. So we let the service layer create it.
        if(currSession == null) return null;


        // check the version, if it is the same then we update the record
        if(currSession.getVersion() == sessionModel.getVersion() || forceUpdate) {
            sessionModel.setVersion(currSession.getVersion() + 1);
            return db.updateSessionWithId(sessionId, sessionModel);
        }

        return null;
    }

    public Boolean removeSessionWithId(String sessionId) {

        // get session so we can get the ID
        SessionModel currSession = getSessionById(sessionId);

        // remove it if the ID is the same
        // This might now work as intended in the mock database. But I believe it should work properly when using the real one.
        return db.removeSession(sessionId, currSession.getVersion());
    }
}
