package com.alfredcode.socialWebsite.service.user;

import org.springframework.stereotype.Service;

import com.alfredcode.socialWebsite.DAO.UserDAO;
import com.alfredcode.socialWebsite.model.UserModel;
import com.alfredcode.socialWebsite.service.user.exception.FailedUserAuthenticationException;
import com.alfredcode.socialWebsite.service.user.exception.FailedUserRegistrationException;

import at.favre.lib.crypto.bcrypt.BCrypt;

/*
 * Manages business logic for user data.
*/
@Service
public class UserService {
    private UserDAO userDao = new UserDAO();
    private static final int minPasswordLength = 4;
    private static final int minUsernameLength = 4;
    private static final char[] illegalPasswordChars = {'+', '*', ';'}; // here we could include sensitive DB characters that could be used for SQL injection
    private static final char[] illegalUsernameChars = {'+', '*', ';'}; // ^


    /**
     * Attempts to register a user
     * @param userModel The user to register
     * @return The user that was persisted in the database (password will be hashed)
     * @throws FailedUserRegistrationException If a business logic rule is broken, or the DAO fails
     * @throws IllegalArgumentException If the user data given is invalid
     */
    public UserModel registerUser(UserModel userModel) throws FailedUserRegistrationException, IllegalArgumentException {

        // validate object
        if(userModel == null) throw new IllegalArgumentException("UserModel cannot be null");
        
        String password = userModel.getPassword();
        String username = userModel.getUsername();

        // data validation
        if(password.length() < minPasswordLength) throw new IllegalArgumentException("Password is too short. Must be " + minPasswordLength + " characters or more.");
        if(username.length() < minUsernameLength) throw new IllegalArgumentException("Username is too short. Must be " + minUsernameLength + " characters or more.");

        // data sanitation
        if(containsChar(password, illegalPasswordChars)) throw new IllegalArgumentException("Password contains one of the following illegal charcters: " + new String(illegalPasswordChars));
        if(containsChar(username, illegalPasswordChars)) throw new IllegalArgumentException("Username contains one of the following illegal charcters: " + new String(illegalUsernameChars));

        // check username availability
        if(userDao.getUserByUsername(username) != null) throw new FailedUserRegistrationException("Username ["+ username +"] is not available.");

        // hash password
        String hashedPassword = BCrypt.withDefaults().hashToString(minPasswordLength, password.toCharArray());
        userModel.setPassword(hashedPassword);
        
        // ask DAO to CREATE user.
        if(userDao.addUser(userModel) == null) throw new FailedUserRegistrationException("Error when registering user.");

        return userModel;
    }


    /**
     * Attempts to authenticate the give nuser
     * @param username The username
     * @param password The password (non-hashed)
     * @throws FailedUserAuthenticationException If a business logic rule is broken, or the DAO fails
     * @throws IllegalArgumentException If the user data given is invalid
     */
    public void authenticateUser(String username, String password) throws FailedUserAuthenticationException, IllegalArgumentException{

        // validate data
        if(username == null || username.isBlank()) throw new IllegalArgumentException("Username is invalid.");
        if(password == null || password.isBlank()) throw new IllegalArgumentException("Password is invalid.");

        // attempt to retrieve user
        UserModel user = userDao.getUserByUsername(username);

        // if no user was retrieved, throw ex
        // TODO: right now all the DAO layers throws a null for any kind of error, we need to work on DAO exceptions later
        if(user == null) throw new FailedUserAuthenticationException("Incorrect username.");

        // if password is erroneous, throw ex
        if(!BCrypt.verifyer().verify(password.toCharArray(), user.getPassword().toCharArray()).verified) throw new FailedUserAuthenticationException("Incorrect password.");
    }

    /**
     * Checks if str contains any character from chars[]
     * @param str The string we whish to analyze
     * @param chars An array of characters we want to check agains the string
     * @return True if any of the characters is contained within the string
     */
    private boolean containsChar(String str, char[] chars) {

        for(char sc : str.toCharArray()) {

            for( char c : chars) {

                if( c == sc) return true;
            }
        }

        return false;
    }
}
