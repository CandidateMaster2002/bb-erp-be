package com.boltblazers.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BbErpBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BbErpBeApplication.class, args);
	}

}
