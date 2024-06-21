package com.alfredcode.socialWebsite.DAO;

import com.alfredcode.socialWebsite.Models.SessionModel;

/*
 * This DAO is a bit different, we dont have a SessionService so things like checking if there is already a session in place fall
 * into this class' responsabilities.
 * 
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
public class SessionDAO {
    
    public SessionModel addSession(SessionModel sessionModel) {

        // check if there is no other session out there for this user, if there is then use that one and do not create another one

        return null;
    }

    public SessionModel getSessionById(String sessionId) {
        return null;
    }

    public SessionModel getSessionByUsername(String username) {
        return null;
    }

    // null if nothing got modified.
    public SessionModel updateSessionWithId(String sessionId, SessionModel sessionModel) {

    }
}
