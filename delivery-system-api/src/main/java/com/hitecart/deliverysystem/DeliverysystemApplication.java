package com.hitecart.deliverysystem;

import com.hitecart.deliverysystem.model.DeliveryPointCategory;
import com.hitecart.deliverysystem.model.Role;
import com.hitecart.deliverysystem.model.Utilisateur;
import com.hitecart.deliverysystem.repository.IDeliveryPointCategoryRepository;
import com.hitecart.deliverysystem.repository.RoleRepository;
import com.hitecart.deliverysystem.repository.UtilisateurRepository;
import com.hitecart.deliverysystem.utils.CodeGeneratorExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;


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
	public CommandLineRunner runner(RoleRepository roleRepository, UtilisateurRepository userRepo,
									IDeliveryPointCategoryRepository dpcRepository) {
		return args -> {//CodeGeneratorExecutor.executeCodeGenerator();
			if(roleRepository.findByName("USER").isEmpty()){
				roleRepository.save(Role.builder().name("USER")
								.createdBy(1)
						.build());

			}

			if(userRepo.findByEmail("admin@gmail.com").isEmpty()){
				userRepo.save(
						Utilisateur.builder()
								.password(passwordEncoder.encode("admin"))
								.email("admin@gmail.com")
								.createdBy(1)
								.enabled(true)
								.firstname("admin")
								.lastname("admin")
								.phoneNum("+3355478798")
								.build()
				);
			}

			if(dpcRepository.findAll().isEmpty()){
				dpcRepository.save(
						DeliveryPointCategory.builder().name("Standard-Range").
						description("Companies in this category offer basic, cost-effective delivery services. Their pricing reflects standard shipping costs without extra premiums.")
								.createdBy(1)
								.additionalFees(0.0)
								.build()
				);
				dpcRepository.save(
						DeliveryPointCategory.builder().name("Mid-Range")
								.description("These companies balance affordability with enhanced security measures. They keep delivery costs moderate while implementing additional protocols to ensure safer transit.")
								.createdBy(1)
								.additionalFees(10.0)
								.build()
				);
				dpcRepository.save(
						DeliveryPointCategory.builder().name("Hight-End")
								.description("Serving high-value goods, these companies charge premium delivery fees. Their services include advanced, secure logistics and specialized handling to match the elevated value of the items.")
								.createdBy(1)
								.additionalFees(20.0)
								.build()
				);
			}
		};
	}
}
