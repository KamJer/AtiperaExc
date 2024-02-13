package com.kamjer.AtiperaExc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AtiperaExcApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtiperaExcApplication.class, args);
	}
}
