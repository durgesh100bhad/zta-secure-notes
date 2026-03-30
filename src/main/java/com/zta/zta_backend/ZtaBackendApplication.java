package com.zta.zta_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class ZtaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZtaBackendApplication.class, args);
	}

}
