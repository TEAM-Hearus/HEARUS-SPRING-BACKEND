package com.hearus.hearusspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HearusSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(HearusSpringApplication.class, args);
	}

}
