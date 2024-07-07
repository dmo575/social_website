package com.alfredcode.socialWebsite;

import java.sql.Statement;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


/*
 * The init method runs after the spring application is up and running. Used to fire some debug methods.
 */
@Component
public class Init {
    private static final Logger logger = LoggerFactory.getLogger(Init.class);
    private DataSource ds = null;

    @Autowired
    public Init(DataSource ds) {
        this.ds = ds;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {

        clearDatabase();
        //populateSession();

    }

    private void clearDatabase() {
        try{

            Connection connection = ds.getConnection();

            Statement st = connection.createStatement();

            st.execute("TRUNCATE TABLE session");
            st.execute("TRUNCATE TABLE user");

            st.close();
        }
        catch(SQLException err) {
            logger.error("clearDatabase::" + err.getMessage());
        }
    }

    private void populateSession() {

        Long now = new Date().getTime();

        try{

            Connection connection = ds.getConnection();
            PreparedStatement st = connection.prepareStatement("INSERT INTO session(id, username, expiration_date_unix, refresh_date_unix) VALUES(?, ?, ?, ?)");

            for(int i = 0; i < 30; i++) {
                st.setString(1, Integer.toString(i));
                st.setString(2, "user_" + Integer.toString(i));
                st.setLong(3, now + 1000*(10+i+1));
                st.setLong(4, now + 1000*(10+i+1));
                st.execute();
            }

            st.close();
        }
        catch(SQLException err) {
            logger.error("populateSession::" + err.getMessage());
        }
    }

}
