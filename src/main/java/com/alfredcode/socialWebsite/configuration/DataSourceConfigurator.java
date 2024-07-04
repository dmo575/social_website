package com.alfredcode.socialWebsite.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfigurator {
    Logger logger = LoggerFactory.getLogger(DataSourceConfigurator.class);
    
    @Bean
    public DataSource createDataSource() {

        Properties protperties = new Properties();
        String databaseIp = null;
        String databasePort = null;
        String databaseName = null;
        String username = null;
        String password = null;

        try{
            protperties.load(new FileInputStream("./sot"));
            databaseIp = protperties.getProperty("database.ip");
            databasePort = protperties.getProperty("database.port");
            databaseName = protperties.getProperty("database.name");
            username = protperties.getProperty("database.username");
            password = protperties.getProperty("database.password");

        }catch(IOException ex) {
            logger.error("Error while configuring Hikari's DataSource: " + ex.getMessage());
        }
        HikariConfig dsConfig = new HikariConfig();

        dsConfig.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s", databaseIp, databasePort, databaseName));
        dsConfig.setUsername(username);
        dsConfig.setPassword(password);

        return new HikariDataSource(dsConfig);
    }
}
