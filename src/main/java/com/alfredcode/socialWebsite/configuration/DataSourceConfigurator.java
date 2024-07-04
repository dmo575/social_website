package com.alfredcode.socialWebsite.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfigurator {
    
    @Bean
    public DataSource createDataSource() {
        return new HikariDataSource();
    }
}
