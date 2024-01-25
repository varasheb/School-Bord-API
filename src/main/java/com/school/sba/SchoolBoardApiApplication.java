package com.school.sba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.school.sba")
public class SchoolBoardApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolBoardApiApplication.class, args);
	}
}
