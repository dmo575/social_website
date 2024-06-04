package com.alfredcode.socialWebsite.Services;

import com.alfredcode.socialWebsite.DAO.UserDAO;
import com.alfredcode.socialWebsite.Exceptions.UserRegistrationException;
import com.alfredcode.socialWebsite.Exceptions.UsernameTakenException;
import com.alfredcode.socialWebsite.Models.UserModel;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class UserService {
    private UserDAO userDao = new UserDAO();
    private static final int minPasswordLength = 4;
    private static final int minUsernameLength = 4;
    private static final char[] bannedPasswordChars = {'+', '*', ';'}; // here we could include sensitive DB characters that could be used for SQL injection
    private static final char[] bannedUsernameChars = {'+', '*', ';'}; // ^


    public UserModel registerUser(UserModel u) {

        // validate object
        if(u == null) throw new IllegalArgumentException("UserModel cannot be null");
        
        String password = u.getPassword();
        String username = u.getUsername();

        // data validation
        if(password.length() < minPasswordLength) throw new IllegalArgumentException("Password is too short. Must be " + minPasswordLength + " characters or more.");
        if(username.length() < minUsernameLength) throw new IllegalArgumentException("Username is too short. Must be " + minUsernameLength + " characters or more.");

        // data sanitation
        if(password.contains(bannedPasswordChars.toString())) throw new IllegalArgumentException("Password contains one of the following banned charcters: " + bannedPasswordChars.toString());
        if(username.contains(bannedUsernameChars.toString())) throw new IllegalArgumentException("Username contains one of the following banned charcters: " + bannedUsernameChars.toString());

        // check if username taken
        if(userDao.getUserByName(u.getUsername()) != null) {
            throw new UsernameTakenException("Username already in use. Try another one.");
        }

        // hash password
        String hashedPassword = BCrypt.withDefaults().hashToString(minPasswordLength, password.toCharArray());
        u.setPassword(hashedPassword);
        
        // ask DAO to CREATE user.
        if(!userDao.createUser(u)) {
            throw new UserRegistrationException("Error when registering user. Please try again.");
        }

        return u;
    }
}
