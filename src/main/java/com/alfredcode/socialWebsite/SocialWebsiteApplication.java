package com.alfredcode.socialWebsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/*
 * We will configure our own DataSource on our configuration class instead of going trough application.properties
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SocialWebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialWebsiteApplication.class, args);
	}

}
