package com.salsel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SalselProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(SalselProjectApplication.class, args);
	}

}
