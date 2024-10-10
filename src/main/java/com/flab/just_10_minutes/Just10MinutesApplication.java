package com.flab.just_10_minutes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Just10MinutesApplication {

	public static void main(String[] args) {
		SpringApplication.run(Just10MinutesApplication.class, args);
	}

}
