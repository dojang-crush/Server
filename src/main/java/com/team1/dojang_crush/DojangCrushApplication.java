package com.team1.dojang_crush;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DojangCrushApplication {

	public static void main(String[] args) {
		SpringApplication.run(DojangCrushApplication.class, args);
	}

}
