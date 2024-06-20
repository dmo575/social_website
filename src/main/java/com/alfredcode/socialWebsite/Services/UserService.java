package com.alfredcode.socialWebsite.Services;

import java.sql.SQLException;

import org.springframework.stereotype.Service;

import com.alfredcode.socialWebsite.DAO.UserDAO;
import com.alfredcode.socialWebsite.Exceptions.UserRegistrationException;
import com.alfredcode.socialWebsite.Exceptions.UsernameTakenException;
import com.alfredcode.socialWebsite.Models.UserModel;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Service
public class UserService {
    private UserDAO userDao = new UserDAO();
    private static final int minPasswordLength = 4;
    private static final int minUsernameLength = 4;
    private static final char[] illegalPasswordChars = {'+', '*', ';'}; // here we could include sensitive DB characters that could be used for SQL injection
    private static final char[] illegalUsernameChars = {'+', '*', ';'}; // ^


    public UserModel registerUser(UserModel u) throws UsernameTakenException, IllegalArgumentException, UserRegistrationException {

        // validate object
        if(u == null) throw new IllegalArgumentException("UserModel cannot be null");
        
        String password = u.getPassword();
        String username = u.getUsername();

        // data validation
        if(password.length() < minPasswordLength) throw new IllegalArgumentException("Password is too short. Must be " + minPasswordLength + " characters or more.");
        if(username.length() < minUsernameLength) throw new IllegalArgumentException("Username is too short. Must be " + minUsernameLength + " characters or more.");

        // data sanitation
        if(containsChar(password, illegalPasswordChars)) throw new IllegalArgumentException("Password contains one of the following illegal charcters: " + new String(illegalPasswordChars));
        if(containsChar(username, illegalPasswordChars)) throw new IllegalArgumentException("Username contains one of the following illegal charcters: " + new String(illegalUsernameChars));

        // check username availability
        if(userDao.getUserByUsername(username) != null) throw new UsernameTakenException("Username ["+ username +"] is not available.");

        // hash password
        String hashedPassword = BCrypt.withDefaults().hashToString(minPasswordLength, password.toCharArray());
        u.setPassword(hashedPassword);
        
        // ask DAO to CREATE user.
        if(!userDao.addUser(u)) throw new UserRegistrationException("Error when registering user.");

        return u;
    }

    // checks if "str" contains any of "chars" characters
    private boolean containsChar(String str, char[] chars) {

        for(char sc : str.toCharArray()) {

            for( char c : chars) {

                if( c == sc) return true;
            }
        }

        return false;
    }
}
