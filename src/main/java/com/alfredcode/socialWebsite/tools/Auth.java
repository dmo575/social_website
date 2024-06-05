package com.alfredcode.socialWebsite.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alfredcode.socialWebsite.DAO.UserDAO;
import com.alfredcode.socialWebsite.Exceptions.FailedSessionAuthenticationException;
import com.alfredcode.socialWebsite.Exceptions.FailedUserAuthenticationException;
import com.alfredcode.socialWebsite.Models.UserModel;

import at.favre.lib.crypto.bcrypt.BCrypt;

import jakarta.servlet.http.HttpServletResponse;

// manages authentication of user and session
// creates sessions
public class Auth {

    private static final int randomSectionLength = 16;
    private static final int sessionExpirationTime = 5;
    private static final int hashingCost = 4;

    private static final Logger logger = LoggerFactory.getLogger(Auth.class);
    private static UserDAO userDao = new UserDAO();


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
    }

    // given a date object, returns its HTTP counterpart
    private static String dateToHTTPDate(Date d) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        return dateFormat.format(d);
    }

    // given a date and an offset of time in hours, returns new offseted date
    private static Date offsetDate(Date d, int offset) {
        
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(d);
        calendar.add(Calendar.HOUR_OF_DAY, offset);

        return calendar.getTime();
    }

    // given an HTTP formated date, returns its Date object counterpart
    private static Date HTTPDateToDate(String d) throws ParseException{

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ");

        return dateFormat.parse(d);
    }

    // authenticates the session and re-issues the sessionId
    public static boolean authenticateSession(String sessionId, HttpServletResponse res) {

        // get session data
        SessionData sessionData = userDao.getSessionById(sessionId);

        // if we cannot find session with provided sessionId, fail authentication
        if(sessionData == null) return false; //throw new FailedSessionAuthenticationException("Invalid session ID");
        
        // check that the session is not expired
        Date currentTime = new Date();
        Date currentExpiration = sessionData.getExpiration();

        // if no expiration data, fail authentication
        if(currentExpiration == null) return false; //throw new FailedSessionAuthenticationException("Invalid session data.");

        // if session expired, fail authentication
        if(currentTime.compareTo(currentExpiration) > 0) return false; //throw new FailedSessionAuthenticationException("Session expired.");

        // update session
        userDao.removeSession(sessionId);
        setSession(sessionData.getUsername(), res);

        return true;
    }

    // authenticates user credentials
    public static void authenticateUser(String username, String password) {

        // QUERY user with DAO
        UserModel user = userDao.getUserByName(username);

        // validate 
        if(user == null) throw new FailedUserAuthenticationException("Incorrect username.");

        // authenticate
        if(!BCrypt.verifyer().verify(password.toCharArray(), user.getPassword().toCharArray()).verified) throw new FailedUserAuthenticationException("Incorrect passwprd.");
    }
}