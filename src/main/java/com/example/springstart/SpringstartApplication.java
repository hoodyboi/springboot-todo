package com.example.springstart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@SpringBootApplication
public class SpringstartApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringstartApplication.class, args);
	}

}
