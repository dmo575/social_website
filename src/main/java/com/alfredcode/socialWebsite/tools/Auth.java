package com.alfredcode.socialWebsite.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.RandomStringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alfredcode.socialWebsite.DAO.UserDAO;
import com.alfredcode.socialWebsite.controller.AccessController;
import com.alfredcode.socialWebsite.exception.FailedAuthenticationException;
import com.alfredcode.socialWebsite.exception.FailedSessionAuthenticationException;
import com.alfredcode.socialWebsite.exception.FailedSessionCreationException;
import com.alfredcode.socialWebsite.exception.FailedUserAuthenticationException;
import com.alfredcode.socialWebsite.model.SessionModel;
import com.alfredcode.socialWebsite.model.UserModel;
import com.alfredcode.socialWebsite.service.SessionService;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/*
 * Manages user and session authentication and authorization
 */
@Aspect
@Component
public class Auth {

    private static final Logger logger = LoggerFactory.getLogger(Auth.class);

    // defines the length of the first section of the sessionId
    private static final int randomSectionLength = 16;
    // defines the lifespan of a session ID in hours
    private static final int sessionExpirationTimeHours = 2;
    // defines the lifespan of a session ID's refresh
    private static final int sessionRefreshTimeMinutes = 15;
    // used by ByCrypt, the higher the more iterations on the hashing computation, which seems to make it more secure but also means it takes more to compute
    private static final int hashingCost = 4;

    private static UserDAO userDao = new UserDAO();
    private static SessionService sessionService = new SessionService();

    @Autowired
    public Auth(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    {
        // TODO: start a thread that empties expired sessions every [sessionExpirationTimeHours] time. Then log that you did just that to the console.
    }

    /**
     * Given a Date object, returns an HTTP formatter date as a String
     * 
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
     * 
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
     * Given an HTTP formatted date string, return a Date object
     * 
     * @param httpData An HTTP date in string format
     * @return Ahe date as a Date object
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

    /**
     * Given an ISO8601 date, returns a Date object
     * 
     * @param iso8601Date An ISO8601 date in string format
     * @return The date as a Date object
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

    /**
     * Given a username, returns a session ID (not registered)
     * 
     * @param username The username we want to produce a session ID for
     * @return A session ID valid to the given username. Not registered.
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

    /**
     * Given a session ID and an expiration date in HTTP format, returns a cookie for that session to be used with Set-Cookie
     * 
     * @param sessionId The session ID to add to the cookie
     * @param expirationDateHttp The expiration date string for the cookie in HTTP format
     * @return A string representing a session ID cookie, ready to be added to Set-Cookie
     */
    private static String getSessionCookie(String sessionId, String expirationDateHttp) {
        return "sessionId=" + sessionId + "; expires=" + expirationDateHttp + "; path=/";
    }


    /**
     * Confirms that the user exists and the password is correct for that user
     * 
     * @param username The username of the user we want to authenticate
     * @param password The password for that username
     * @throws FailedUserAuthenticationException If the credentials are invalid
     */
    public static void authenticateUser(String username, String password) throws FailedUserAuthenticationException {

        // validate data
        if(username.isEmpty()) throw new IllegalArgumentException("Username field is empty.");
        if(password.isEmpty()) throw new IllegalArgumentException("Password field is empty.");

        // try to get user
        UserModel user = userDao.getUserByUsername(username);

        // if user doesnt exist, throw ex
        if(user == null) throw new FailedUserAuthenticationException("Incorrect username.");

        // if password is erroneous, throw ex
        if(!BCrypt.verifyer().verify(password.toCharArray(), user.getPassword().toCharArray()).verified) throw new FailedUserAuthenticationException("Incorrect password.");
    }


    /**
     * Registers and returns a session cookie for the given username
     * 
     * @param username The username we want to initiate a session for
     * @return The session ID cookie string, ready to be added to a Set-Cookie header
     * @throws FailedSessionCreationException If something goes wrong when adding the session to the database
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
        if(sessionService.addSession(session) == null) throw new FailedSessionCreationException("Error while adding session to database.");

        // return the session's Set-Cookie value
        return getSessionCookie(sessionId, dateToHTTPDate(expirationDate));
    }

    /**
     * Checks if sessionId is valid, updates it and returns the updated session cookie string
     * 
     * @param sessionId The ID of the session we want to authenticate
     * @returns An updated session cookie string (This deprecates the old one). Ready to be used on the Set-Cookie header
     * @throws FailedSessionAuthenticationException If the session authentication fails (expired, non existent...)
     */
    public static String authenticateSession(String sessionId) throws FailedSessionAuthenticationException {

        // get session
        SessionModel session = sessionService.getSessionById(sessionId);

        // if session not found, throw ex
        if(session == null) throw new FailedSessionAuthenticationException("Incorrect session credentials.");

        // get current and session expiration dates
        Date dateNow = new Date();
        Date oldExpirationDate = new Date(session.getExpirationDateUnix());
        Date oldRefreshDate = new Date(session.getExpirationDateUnix());

        // if we are past the session expiration date:
        if(dateNow.compareTo(oldExpirationDate) > 0) throw new FailedSessionAuthenticationException("Session expired.");

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

    /////////////////////////////////////////////////////////////////////////////
    @Before("@annotation(com.alfredcode.socialWebsite.security.Annotations.SessionRequired)")
    private void sessionRequired(JoinPoint jp) {

        /*
         * Each time this thread is processing an HTTP request trough a controller method, information about that current task can be accessed trough the 
         * RequestContextholder class.
         * The getRequestAttributes allows us to access the request and response objects, which is what makes this great, else we would have to
         * have the controller method declare a HttpServletRequest parameter every time at the same place to then access it trough args[] 
         */

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // will store the sessionId if any
        String sessionId = null;
        Cookie[] cookies = request.getCookies();

        if(cookies != null) {
            // go trough the cookies and retrieve the sessionId
            for(Cookie c : request.getCookies()) {
                if(c.getName().equals("sessionId")) {
                    sessionId = c.getValue();
                    break;
                }
            }
        }

        // if no sessionId, throw ex
        if(sessionId == null || sessionId.isBlank()) {/* throw exception */}

        // authenticate sessionId
        logger.info("SESSION_ID: " + sessionId);

        // authorize sessionId (TODO, we dont have sequrity levels right now)

        // use sessionService to check if the session is valid

        // throw ex if not valid
    }

    @Before("@annotation(com.alfredcode.socialWebsite.security.Annotations.NoSessionAllowed)")
    private void noSessionAllowed(JoinPoint jp) {
        // use sessionService to check if the session is valid
        // throw ex if valid
    }

}