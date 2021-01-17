package com.maestro.app.sample.ms.events;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;


@SpringBootApplication
@EnableOAuth2Client
public class LogsServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(LogsServiceApplication.class, args);
	}
}