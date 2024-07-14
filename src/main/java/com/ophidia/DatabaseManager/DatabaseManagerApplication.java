package com.ophidia.DatabaseManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DatabaseManagerApplication {

	public static void main(String[] args) {
		ServiceFacade.getInstance().StartServices();
		SpringApplication.run(DatabaseManagerApplication.class, args);

	}

}
