package com.example.nuestro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
public class NuestroApplication {
	private static final Logger LOGGER= LoggerFactory.getLogger(NuestroApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(NuestroApplication.class, args);
		LOGGER.info("Nuestro app is running");
	}
}
