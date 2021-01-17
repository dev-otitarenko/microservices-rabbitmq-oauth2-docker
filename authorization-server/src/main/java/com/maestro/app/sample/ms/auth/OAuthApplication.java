package com.maestro.app.sample.ms.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextListener;
import ua_parser.Parser;

import java.io.IOException;

@SpringBootApplication
public class OAuthApplication {
	public static void main(String[] args) {
		SpringApplication.run(OAuthApplication.class, args);
	}

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}

	@Bean
	public Parser uaParser() throws IOException {
		return new Parser();
	}
}