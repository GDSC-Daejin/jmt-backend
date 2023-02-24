package com.gdsc.jmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JmtApplication {

	public static void main(String[] args) {
		SpringApplication.run(JmtApplication.class, args);
	}

}
