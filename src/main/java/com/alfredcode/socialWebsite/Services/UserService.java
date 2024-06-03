package com.alfredcode.socialWebsite.Services;

import com.alfredcode.socialWebsite.DAO.UserDAO;
import com.alfredcode.socialWebsite.Exceptions.UserRegistrationException;
import com.alfredcode.socialWebsite.Exceptions.UsernameTakenException;
import com.alfredcode.socialWebsite.Models.UserModel;

public class UserService {
    private UserDAO userDao = new UserDAO();


    public UserModel registerUser(UserModel u) {

        // TODO:
        // double check that what you already checked on the client regarding username and password requirements
        // you would also want to sanitize the input here before sending it to the DAO

        // TODO:
        // after clearing user data, we start using it in our checks:
        // is username taken? If not:
        // use DAO (for now all in service layer tho) to CREATE user record, then create a sessionId
        // send both back

        // is argument valid
        if(u == null) {
            throw new IllegalArgumentException("User not provided.");
        }

        // check if username taken
        if(userDao.getUserByName(u.getName()) != null) {
            throw new UsernameTakenException("Username already in use. Try another one.");
        }
        
        // TODO: here we will also want to hash the password before storing it
        // . . .

        // ask DAO to CREATE user.
        if(!userDao.createUser(u)) {
            throw new UserRegistrationException("Error when registering user. Please try again.");
        }

        return u;
    }
}
