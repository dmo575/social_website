package com.alfredcode.socialWebsite;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Init {
    private static final Logger logger = LoggerFactory.getLogger(Init.class);
    private DataSource ds = null;

    @Autowired
    public Init(DataSource ds) {
        this.ds = ds;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void clearDatabase() {

        boolean doit = false;

        if(doit) return;

        try{

            Connection connection = ds.getConnection();

            Statement st = connection.createStatement();

            st.execute("TRUNCATE TABLE session");
            st.execute("TRUNCATE TABLE user");

            st.close();
        }
        catch(SQLException err) {
            logger.error("Init::" + err.getMessage());
        }
    }

}
