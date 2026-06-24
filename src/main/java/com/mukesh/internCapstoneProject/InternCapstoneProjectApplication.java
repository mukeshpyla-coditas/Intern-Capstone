package com.mukesh.internCapstoneProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class InternCapstoneProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(InternCapstoneProjectApplication.class, args);
	}

}
