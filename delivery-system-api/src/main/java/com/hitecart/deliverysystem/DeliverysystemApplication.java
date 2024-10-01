package com.hitecart.deliverysystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DeliverysystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliverysystemApplication.class, args);
	}

}
