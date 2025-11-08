package com.twilight.twilight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TwilightApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwilightApplication.class, args);
	}


}
