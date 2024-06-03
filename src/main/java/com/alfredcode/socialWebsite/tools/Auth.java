package com.alfredcode.socialWebsite.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.http.RequestEntity;

import com.alfredcode.socialWebsite.Controllers.FrontEndController;
import com.alfredcode.socialWebsite.DAO.UserDAO;
import com.alfredcode.socialWebsite.Exceptions.AuthenticationFailedException;
import com.alfredcode.socialWebsite.Models.UserModel;

import at.favre.lib.crypto.bcrypt.BCrypt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Auth {

    private static int randomSectionLength = 16;
    private static int sessionExpirationTime = 5;
    private static final Logger logger = LoggerFactory.getLogger(Auth.class);
    private static UserDAO userDao = new UserDAO();


    // it sets a session cookie for the given user in the given response servlet.
    public static void setSession(String username, HttpServletResponse res) {

        logger.warn("a");
        // get session ID
        String sessionRandom = RandomStringUtils.randomAlphanumeric(randomSectionLength);

        // this makes the sessionId different every time for the same user
        // it also makes sure that each sessionId is unique since users cannot share the same username
        String sessionIdString = sessionRandom + username;

        String sessionId = BCrypt.withDefaults().hashToString(4, sessionIdString.toCharArray());

        logger.warn("B");

        //boolean result = BCrypt.verifyer().verify(sessionIdString.toCharArray(), sessionId.toCharArray()).verified;

        // get date on HTTP standards
        Date expires = offsetDate(new Date(), sessionExpirationTime);
        String HttpExpires = dateToHTTPDate(expires);
        logger.warn("C");

        // set cookie
        res.addHeader("Set-Cookie", "sessionId=" + sessionId + "; expires=" + HttpExpires + "; path=/");
        logger.warn("D");

        //register a new session
        userDao.setSession(username, sessionId, expires);
        logger.warn("E");
    }

    private static String dateToHTTPDate(Date d) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        return dateFormat.format(d);
    }

    private static Date offsetDate(Date d, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.HOUR_OF_DAY, offset);
        return calendar.getTime();
    }

    private static Date HTTPDateToDate(String d) throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ");

        return dateFormat.parse(d);
    }


    public static boolean authenticateUser(String sessionId, HttpServletResponse res) {

        SessionData sessionData = userDao.getSessionById(sessionId);

        logger.warn("1: " + sessionData);
        // check that the session exists
        if(sessionData == null) { return false; }
        
        // check that the session is not expired
        Date currentTime = new Date();
        Date currentExpiration = sessionData.getExpiration();
        logger.warn("2");

        if(currentExpiration == null) { return false; }

        logger.warn("3");
        logger.warn("Current: " + currentTime.toString());
        logger.warn("Expiration: " + currentExpiration.toString());

        if(currentTime.compareTo(currentExpiration) > 0) { return false; }

        logger.warn("4");
        // update session
        userDao.removeSession(sessionId);
        setSession(sessionData.getUsername(), res);

        return true;
    }
}





        // ways this can go wrong:
        // - someone can use RandomStringUtils.randomAlphanumeric to generate sessionIds,
        // if they know how the generation works, they might be able to reduce the possible ids, they also need to know the length that
        // Im using but that is easy just register and check it out. Since we don't delete expired sessionId records, you could eventually brute force into an account
        // if that account sits inactive long enough (meaning no refresh of the sessionId so more time to apply the brute force)
        // attacker must know:
        // - method to generate the sessionId: RandomStringUtils.randomAlphanumeric
        // - parameters to method, length in this case: 16
        // - target username
        // - knowledge that we don't clear expired sessionIds, we just override on new login
        // we can however, save both the sessionId and its expiration date. then even if we don't clear them, they wont be of use.
        // that would make any efforts to try to brute force into an inactive account almost useless. They would only have as much time as the expiration date says to 
        // come up with the correct sessionId.
        // we can add a maximun number of requests per IP to prevent the brute force attack on this time spam, we just make sure to allow enough requests so that human
        // users have no problem with the page.
        // with brute foce attacks there is also this easy solution: increase the pass length
        // assuming the pool of possible numbers cannot be reduced by the attachers, then that solution plus limiting the ammount of requests per IP should be definitive wall
        // There are other things an attacker may try however:
        // - package pfishing (im writing that wrong); reading the sessionId from an intercepterd HTTP message between client-server, for this HTTPS is recommended over HTTP
        // - as mentioned already, reducing the pool. This is by cracking the pool generation method we use
        // - accessing the server and just reading the sessionId stored
        // - accessing the server and reading the password. This is why we hash them when storing them.
        // - obtaining the password hash, and the tool we use to hash then, and cracking the tool