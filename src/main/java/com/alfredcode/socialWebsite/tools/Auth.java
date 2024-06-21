package com.alfredcode.socialWebsite.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.naming.AuthenticationException;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alfredcode.socialWebsite.DAO.SessionDAO;
import com.alfredcode.socialWebsite.DAO.UserDAO;
import com.alfredcode.socialWebsite.Exceptions.FailedAuthenticationException;
import com.alfredcode.socialWebsite.Exceptions.FailedSessionAuthenticationException;
import com.alfredcode.socialWebsite.Exceptions.FailedSessionCreationException;
import com.alfredcode.socialWebsite.Exceptions.FailedUserAuthenticationException;
import com.alfredcode.socialWebsite.Models.SessionModel;
import com.alfredcode.socialWebsite.Models.UserModel;
import com.alfredcode.socialWebsite.Services.SessionService;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

// manages authentication of user and session
// creates sessions
public class Auth {

    private static final int randomSectionLength = 16;
    private static final int sessionExpirationTimeHours = 2;
    private static final int sessionRefreshTimeMinutes = 15;

    private static final int hashingCost = 4;

    private static final Logger logger = LoggerFactory.getLogger(Auth.class);
    private static UserDAO userDao = new UserDAO();
    private static SessionService sessionService = new SessionService();

    {
        // start a thread that empties expired sessions every [sessionExpirationTimeHours] time. Then log that you did just that to the console.
    }

/* 
    // it sets a session cookie for the given user in the given response servlet.
    public static void setSession(String username, HttpServletResponse res) {

        // get session ID
        String sessionRandom = RandomStringUtils.randomAlphanumeric(randomSectionLength);

        // this makes the sessionId different every time for the same user
        // it also makes sure that each sessionId is unique since users cannot share the same username
        String sessionIdString = sessionRandom + username;

        // stores the hashes session data
        String sessionId = BCrypt.withDefaults().hashToString(hashingCost, sessionIdString.toCharArray());

        // get date on HTTP standards
        Date expires = offsetDate(new Date(), sessionExpirationTime);
        String HttpExpires = dateToHTTPDate(expires);

        // set cookie
        res.addHeader("Set-Cookie", "sessionId=" + sessionId + "; expires=" + HttpExpires + "; path=/");

        //register a new session
        userDao.setSession(username, sessionId, expires);
    } */

    /*
     * Given a Date object, returns an HTTP formatter date as a String
     */
    private static String dateToHTTPDate(Date date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        return dateFormat.format(date);
    }

    /*
     * Given a date, a time offset and a time scale (Calendar.HOUR_OF_DAY for example), returns the offseted Date object
     */
    private static Date offsetDate(Date date, int timeOffset, int timeScale) {
        
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(timeScale, timeOffset);

        return calendar.getTime();
    }

    /*
     * Given an HTTP formatted date string, return a Date object
     */
    private static Date HTTPDateToDate(String httpDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ");

        try {
            return dateFormat.parse(httpDate);
        }
        catch(ParseException ex) {
            throw new FailedAuthenticationException("Error parsing HTTP date to Date: " + ex.getMessage());
        }
    }

    /*
     * Given an ISO8601 date, returns a Date object
     */
    private static Date ISO8601DateToDate(String iso8601Date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

        try {
            return dateFormat.parse(iso8601Date);
        }
        catch(ParseException ex) {
            throw new FailedAuthenticationException("Error parsing ISO8601 date to Date: " + ex.getMessage());
        }
    }
/* 
    // authenticates the session and re-issues the sessionId
    public static boolean authenticateSession(String sessionId, HttpServletResponse res) {

        // get session data
        SessionModel sessionData = userDao.getSessionByHash(sessionId);

        // if we cannot find session with provided sessionId, fail authentication
        if(sessionData == null) return false;
        
        // check that the session is not expired
        Date currentTime = new Date();
        Date currentExpiration = sessionData.getExpiration();

        // if no expiration data, fail authentication
        if(currentExpiration == null) return false;

        // if session expired, fail authentication
        if(currentTime.compareTo(currentExpiration) > 0) return false;

        // update session
        userDao.removeSession(sessionId);
        setSession(sessionData.getUsername(), res);

        return true;
    } */

   /*  // authenticates user credentials
    public static void authenticateUser(String username, String password) throws FailedUserAuthenticationException {

        // QUERY user with DAO
        UserModel user = userDao.getUserByUsername(username);

        // validate 
        if(user == null) throw new FailedUserAuthenticationException("Incorrect username.");

        // authenticate
        if(!BCrypt.verifyer().verify(password.toCharArray(), user.getPassword().toCharArray()).verified) throw new FailedUserAuthenticationException("Incorrect password.");
    } */


    /*
     * Given a username, returns a session ID (unusable alone, you still have to register it)
     */
    private static String getSessionId(String username) {

        // get a random string of charcter as an artifact for the sessionID
        String sessionRandom = RandomStringUtils.randomAlphanumeric(randomSectionLength);

        // this makes the sessionId different every time for the same user
        // it also makes sure that each sessionId is unique since users cannot share the same username
        String sessionIdString = sessionRandom + username;

        // stores the hashes session data
        String sessionId = BCrypt.withDefaults().hashToString(hashingCost, sessionIdString.toCharArray());

        return sessionId;
    }

    /*
     * given a session ID and an expiration date in HTTP format, returns a cookie for that session to be used with Set-Cookie
     */
    private static String getSessionCookie(String sessionId, String expirationDateHttp) {
        return "sessionId=" + sessionId + "; expires=" + expirationDateHttp + "; path=/";
    }


    /*
     * Confirms that the user exists and the password is correct for that user
     * Sends a 303 redirect if the authentication fails
     */
    public static void authenticateUser(String username, String password, String redirect) throws FailedUserAuthenticationException {

        // QUERY user with DAO
        UserModel user = userDao.getUserByUsername(username);

        // if user doesnt exist, throw ex
        if(user == null) throw new FailedUserAuthenticationException("Incorrect username.", redirect);

        // if password is incorrect, throw ex
        if(!BCrypt.verifyer().verify(password.toCharArray(), user.getPassword().toCharArray()).verified) throw new FailedUserAuthenticationException("Incorrect password.", redirect);        
    }

    /*
     * Confirms that the user exists and the password is correct for that user
     * Sends a 401 unauthorized if the authentication fails
     */
    public static void authenticateUser(String username, String password) {
        authenticateUser(username, password, null);
    }

    /*
     * Returns a session cookie for the given username
     * - Generates a session ID
     * - Adds it to the database
     * - Returns session cookie to be used with the Set-Cookie header
     */
    public static String initiateSession(String username) throws FailedSessionCreationException {

        // we get a session ID
        String sessionId = getSessionId(username);

        // calculate expiration dates
        Date dateNow = new Date();
        Date expirationDate = offsetDate(dateNow, sessionExpirationTimeHours, Calendar.HOUR_OF_DAY);
        Date refreshDate = offsetDate(dateNow, sessionRefreshTimeMinutes, Calendar.MINUTE);

        // we store the date in Unix time so we can convert it between a numeric value (Unix) to Date to HTTP. This also
        // allows us to select sessions based on a timeframe (useful when deleting expired sessions).
        SessionModel session = new SessionModel(sessionId, username, expirationDate.getTime(), refreshDate.getTime());

        // if the session cannot be added, throw ex
        // This could later be converted into a try-catch where we just catch any SQL error, analyze it and convert it into a more detailed Auth exception that we can throw
        // to a global exception handler. For now any potential error from the database is just being communicated as a null return from the DAO methods.
        if(sessionService.addSession(session) == null) throw new FailedSessionCreationException("Error while adding session to database.");

        // return the session's Set-Cookie value
        return getSessionCookie(sessionId, dateToHTTPDate(expirationDate));
    }

    /*
     * Returns an updated session. Redirects if the session is expired
     * Checks if the session is valid, updates it if so
     * Sends a 303 redirect if the session is expired
     * Returns updated session to be used with the Set-Cookie header
     */
    public static String authenticateSession(String sessionId, String redirect) {

        // get session
        SessionModel session = sessionService.getSessionById(sessionId);

        // if session not found, throw ex
        if(session == null) throw new FailedSessionAuthenticationException("Incorrect session credentials.", redirect);
        
        // get current and session expiration dates
        Date dateNow = new Date();
        Date oldExpirationDate = new Date(session.getExpirationDateUnix());
        Date oldRefreshDate = new Date(session.getExpirationDateUnix());

        // if we are past the session expiration date, throw ex
        if(dateNow.compareTo(oldExpirationDate) > 0) throw new FailedSessionAuthenticationException("Session expired.", redirect);

        // if we are past the session ID refresh date. Refresh the session ID.
        if(dateNow.compareTo(oldRefreshDate) > 0) {
            // update model with new id
            session.setId(getSessionId(session.getUsername()));
            // get new refresh date
            Date newRefreshDate = offsetDate(dateNow, sessionRefreshTimeMinutes, Calendar.MINUTE);
            // update model with new refresh rate
            session.setRefreshDateUnix(newRefreshDate.getTime());
        }

        // get new expiration date
        Date newExpirationDate = offsetDate(dateNow, sessionExpirationTimeHours, Calendar.HOUR_OF_DAY);

        // update model with new expiration date
        session.setExpirationDateUnix(newExpirationDate.getTime());

        // update the database with the new data for that session.
        sessionService.updateSessionWithId(sessionId, session);

        // return a new sessionId cookie with the new data
        return getSessionCookie(session.getId(), dateToHTTPDate(newExpirationDate));
    }

    /*
     * Returns an updated session. Throws a 401 if session expired
     * Checks if the session is valid, updates it if so
     * Sends a 401 unauthorized if session expired
     * Returns updated session to be used with the Set-Cookie header
     */
    public static String authenticateSession(String sessionID) {
        return authenticateSession(sessionID, null);
    }


}