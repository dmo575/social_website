package com.alfredcode.socialWebsite.Services;

import com.alfredcode.socialWebsite.Database;
import com.alfredcode.socialWebsite.Models.Check;
import com.alfredcode.socialWebsite.Models.User;

public class UserService {
    private Database db = Database.getInstance();

    // returns a Ceck object containing answer to: username exists?
    public Check check_usernameExists(String username) {
        return new Check(db.getUserByName(username) != null);
    }

    // we handle the logic here, we output the results and the controller takes care of what to do with them
    // rules: username must be 5 characters or longer
    // password must be 5 characters or longer
    public User create_user(User u) {
        final int minUsernameLength = 5;
        final int minPasswordLength = 5;

        String username = u.getName();
        String password = u.getPassword();

        if(username.length() < minUsernameLength || password.length() < minPasswordLength)
            return null;

        boolean success = db.users.add(u);

        if(success)
            return u;

        return null;
    }
}
