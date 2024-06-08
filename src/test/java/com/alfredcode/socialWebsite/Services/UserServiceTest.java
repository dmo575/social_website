package com.alfredcode.socialWebsite.Services;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.alfredcode.socialWebsite.Database;
import com.alfredcode.socialWebsite.Models.UserModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserServiceTest {
    
    // class to test
    UserService userService = new UserService();

    @Test
    public void registerUserWrongDataTest() throws JsonProcessingException{

        // wipe any possible data in the database from previous tests
        Database.getInstance().wipeData();
        
        // represents a faulty json (username field incorrectly defined as user)
        String faultyJson = "{ \"user\" : \"value\", \"password\" : \"value\" }";
        
        // read json into UserModel
        ObjectMapper objectMapper = new ObjectMapper();
        UserModel user = objectMapper.readValue(faultyJson, UserModel.class);
        
        // attempt to register the user
        //Exception ex = assertThrows(IllegalArgumentException.class, userService.registerUser(user));
    }
}
