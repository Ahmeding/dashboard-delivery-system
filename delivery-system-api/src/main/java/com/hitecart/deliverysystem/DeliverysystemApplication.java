package com.hitecart.deliverysystem;

import com.hitecart.deliverysystem.model.Role;
import com.hitecart.deliverysystem.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class DeliverysystemApplication {

	public static void main(String[] args) {SpringApplication.run(DeliverysystemApplication.class, args);}

	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository) {
		return args -> {
			if(roleRepository.findByName("USER").isEmpty()){
				roleRepository.save(Role.builder().name("USER")
								.createdBy(1)
						.build());
			}
		};
	}
}
