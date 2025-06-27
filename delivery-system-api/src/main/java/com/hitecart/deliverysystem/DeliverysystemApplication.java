package com.hitecart.deliverysystem;

import com.hitecart.deliverysystem.dao.UserRepository;
import com.hitecart.deliverysystem.model.DeliveryPointCategory;
import com.hitecart.deliverysystem.model.Role;
import com.hitecart.deliverysystem.model.User;
import com.hitecart.deliverysystem.dao.DeliveryPointCategoryRepository;
import com.hitecart.deliverysystem.dao.RoleRepository;
import com.hitecart.deliverysystem.model.enums.RoleType;
import com.hitecart.deliverysystem.services.impl.DriverServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;


@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableCaching
@RequiredArgsConstructor
public class DeliverysystemApplication {


	private final PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(DeliverysystemApplication.class, args);
	}



	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository,
									UserRepository userRepo,
									DeliveryPointCategoryRepository dpcRepository,
									DriverServiceImpl driverService) {
		return args -> {//CodeGeneratorExecutor.executeCodeGenerator();

			User admindUser =  userRepo.findByEmail("admin@gmail.com")
					.orElseGet( () ->{
							Role adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN)
									.orElseGet(()->
									roleRepository.save(
											Role.builder()
													.name(RoleType.ROLE_ADMIN)
													.createdBy(0)
													.build()
									));
								return userRepo.save(
								User.builder()
										.email("admin@gmail.com")
										.password(passwordEncoder.encode("admin"))
										.firstname("Admin")
										.lastname("Admin")
										.phoneNbr("+1234567890")
										.roles(List.of(adminRole))
										.enabled(true)
										.createdBy(0)
										.build()
								);
							}
							);

			Role userRole = roleRepository.findByName(RoleType.ROLE_USER).orElseGet(()->{
				return roleRepository.save( Role.builder()
								 .name(RoleType.ROLE_USER)
								 .createdBy(admindUser.getId())
								 .build());}
			);

			userRepo.findByEmail("user@gmail.com").orElseGet(()->{
				return userRepo.save(
						User.builder()
								.password(passwordEncoder.encode("admin"))
								.email("user@gmail.com")
								.createdBy(admindUser.getId())
								.enabled(true)
								.firstname("user")
								.lastname("user")
								.phoneNbr("+3322785698")
								.roles(List.of(userRole))
								.build()
				);
			});
			if(dpcRepository.findAll().isEmpty()){
				dpcRepository.save(
						DeliveryPointCategory.builder()
								.name("Standard-Range")
								.description("Companies in this category offer basic, cost-effective delivery services. Their pricing reflects standard shipping costs without extra premiums.")
								.createdBy(admindUser.getId())
								.additionalFees(0.0)
								.build()
				);
				dpcRepository.save(
						DeliveryPointCategory.builder().name("Mid-Range")
								.description("These companies balance affordability with enhanced security measures. They keep delivery costs moderate while implementing additional protocols to ensure safer transit.")
								.createdBy(admindUser.getId())
								.additionalFees(10.0)
								.build()
				);
				dpcRepository.save(
						DeliveryPointCategory.builder().name("Hight-End")
								.description("Serving high-value goods, these companies charge premium delivery fees. Their services include advanced, secure logistics and specialized handling to match the elevated value of the items.")
								.createdBy(admindUser.getId())
								.additionalFees(20.0)
								.build()
				);
			}
			driverService.initializeMissingStats();
		};
	}
}
