package com.alfredcode.socialWebsite.service.session;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties.Cache.Connection;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alfredcode.socialWebsite.DAO.SessionDAO;
import com.alfredcode.socialWebsite.model.SessionModel;
import com.alfredcode.socialWebsite.service.session.exception.FailedSessionAuthenticationException;
import com.alfredcode.socialWebsite.service.session.exception.FailedSessionCreationException;
import com.alfredcode.socialWebsite.service.session.exception.FailedSessionUpdateException;

import at.favre.lib.crypto.bcrypt.BCrypt;

/*
 * Manages business logic for session data.
 * 
 * @Scheduled methods:
 * - removeExpiredSessions: Periodically removes expired sessions from the database.
*/
@Service
public class SessionService {

    // defines the length of the first section of the sessionId
    private static final int randomSectionLength = 16;
    // defines the lifespan of a session ID in hours
    private static final int sessionExpirationTimeHours = 2;
    // defines the lifespan of a session ID's refresh
    private static final int sessionRefreshTimeMinutes = 15;
    // used by ByCrypt, the higher the more iterations on the hashing computation, which seems to make it more secure but also means it takes more to compute
    private static final int hashingCost = 7;

    @Autowired
    private SessionDAO sessionDao = null;

    /**
     * Given a Date object, returns an HTTP compliant date string
     * @param date The date to convert to an HTTP date string
     * @return The date in HTTP format as a string
    */
    private static String dateToHTTPDate(Date date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        return dateFormat.format(date);
    }


    /**
     * Given a date, a time offset and a time scale (Calendar.HOUR_OF_DAY for example), returns the offseted Date object
     * @param date The date to offset
     * @param timeOffset The time to offset the date by
     * @param timeScale The time scale of the timeOffset time
     * @return The newly offseted date
    */
    private static Date offsetDate(Date date, int timeOffset, int timeScale) {
        
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(timeScale, timeOffset);

        return calendar.getTime();
    }


    /**
     * Generates a session ID string for the given username
     * @param username The username we want to produce a session ID for
     * @return A session ID string for the given username
    */
    private String generateSessionIdString(String username) {

        // get a random string of charcter as an artifact for the sessionID
        String sessionRandom = RandomStringUtils.randomAlphanumeric(randomSectionLength);

        // this makes the sessionId different every time for the same user
        // it also makes sure that each sessionId is unique since users cannot share the same username
        String sessionIdString = sessionRandom + username;

        // stores the hashes session data
        String sessionId = BCrypt.withDefaults().hashToString(hashingCost, sessionIdString.toCharArray());

        return sessionId;
    }


    /**
     * Generates a cookie string for the given session ID
     * @param sessionId The session ID for the cookie
     * @param expirationDateHttp The expiration date string for the cookie in HTTP format
     * @return A string representing a session ID cookie, ready to be added to Set-Cookie
    */
    private static String generateSessionCookieString(String sessionId, String expirationDateHttp) {
        return "sessionId=" + sessionId + "; expires=" + expirationDateHttp + "; path=/";
    }


    /**
     * Checks if sessionId is valid
     * @param sessionId The ID of the session we want to authenticate
     * @throws FailedSessionAuthenticationException If a business logic rule is broken, or the DAO fails
     * @throws IllegalArgumentException If the data given is invalid
    */
    public void authenticateSession(String sessionId) throws FailedSessionAuthenticationException, IllegalArgumentException {

        // data validation
        if(sessionId == null || sessionId.isBlank()) throw new IllegalArgumentException("SessionId value is invalid.");

        // get session
        SessionModel session = sessionDao.getSessionById(sessionId);

        // if session not found, throw ex
        if(session == null) throw new FailedSessionAuthenticationException("Session credentials are invalid or session has expired.");

        // get current and session expiration dates
        Date dateNow = new Date();
        Date expirationDate = new Date(session.getExpirationDateUnix());

        // if we are past the session expiration date, throw ex
        if(dateNow.compareTo(expirationDate) > 0) throw new FailedSessionAuthenticationException("Session expired.");
    }

    /**
     * Attempts to update an existing session in the Database
     * @param sessionId The current session ID of the session
     * @return A new session cookie string
     * @throws FailedSessionUpdateException If a business logic rule is broken, or the DAO fails
    */
    public String updateSession(String sessionId) {

        // get session
        SessionModel session = sessionDao.getSessionById(sessionId);

        // if failure when retrieving db session's data, throw ex
        if(session == null) throw new FailedSessionUpdateException("Session not found.");

        
        // get dates (current, session's refresh)
        Date dateNow = new Date();
        Date oldRefreshDate = new Date(session.getExpirationDateUnix());

        // if we are past the session ID refresh date: refresh it
        if(dateNow.compareTo(oldRefreshDate) > 0) {
            // generate a new session ID for the user
            session.setId(generateSessionIdString(session.getUsername()));
            // calculate new refresh date
            Date newRefreshDate = offsetDate(dateNow, sessionRefreshTimeMinutes, Calendar.MINUTE);
            // update session's refresh rate
            session.setRefreshDateUnix(newRefreshDate.getTime());
        }

        // get new expiration date
        Date newExpirationDate = offsetDate(dateNow, sessionExpirationTimeHours, Calendar.HOUR_OF_DAY);

        // update session's expiration date
        session.setExpirationDateUnix(newExpirationDate.getTime());

        // update the database's session
        session = sessionDao.updateSession(session);

        // if failure when updating db session's record, throw ex
        if(session == null) throw new FailedSessionUpdateException("Failure when updating session.");

        // return the updated sessionId cookie string
        return generateSessionCookieString(session.getId(), dateToHTTPDate(newExpirationDate));
    }


    /**
     * Attempts to initiate a session
     * @param username The username this session is for
     * @return A new session cookie string
     * @throws FailedSessionCreationException If a business logic rule is broken, or the DAO fails
    */
    public String initiateSession(String username) {

        // check if the username already has a session in storage
        SessionModel session = sessionDao.getSessionByUsername(username);

        // remove it if so
        if(session != null) sessionDao.removeSessionWithId(session.getId());

        // calculate dates
        Date dateNow = new Date();
        Date expirationDate = offsetDate(dateNow, sessionExpirationTimeHours, Calendar.HOUR_OF_DAY);
        Date refreshDate = offsetDate(dateNow, sessionRefreshTimeMinutes, Calendar.MINUTE);

        // create session model
        session = new SessionModel(generateSessionIdString(username),username, expirationDate.getTime(), refreshDate.getTime());

        // attempt to create session record
        session = sessionDao.addSession(session);

        // if the database failed to record the session, throw ex
        if(session == null) throw new FailedSessionCreationException("Session could not be persisted.");

        // return new session cookie string
        return generateSessionCookieString(session.getId(), dateToHTTPDate(expirationDate));
    }

}
