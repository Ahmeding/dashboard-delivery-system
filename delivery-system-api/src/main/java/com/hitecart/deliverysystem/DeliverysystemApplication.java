package com.hitecart.deliverysystem;

import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.model.DeliveryPointCategory;
import com.hitecart.deliverysystem.model.Role;
import com.hitecart.deliverysystem.model.Utilisateur;
import com.hitecart.deliverysystem.dao.IDeliveryPointCategoryRepository;
import com.hitecart.deliverysystem.dao.RoleRepository;
import com.hitecart.deliverysystem.dao.UtilisateurRepository;
import com.hitecart.deliverysystem.services.impl.ChauffeurServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
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
									UtilisateurRepository userRepo,
									IDeliveryPointCategoryRepository dpcRepository,
									ChauffeurServiceImpl driverService) {
		return args -> {//CodeGeneratorExecutor.executeCodeGenerator();

			Utilisateur connectedUser =  userRepo.findByEmail("admin@gmail.com").orElseThrow(() ->new EntityNotFoundException("not found user"));
			Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(()->{
				return roleRepository.save( Role.builder()
								 .name("ROLE_USER")
								.createdBy(1)
								 .build());}
			);

			List<Role> roleList  =  new ArrayList<>();
			roleList.add(userRole);

			if(userRepo.findByEmail("admin3@gmail.com").isEmpty()){
				userRepo.save(
						Utilisateur.builder()
								.password(passwordEncoder.encode("admin"))
								.email("admin3@gmail.com")
								.createdBy(connectedUser.getId())
								.enabled(true)
								.firstname("Admin3")
								.lastname("Admin")
								.phoneNum("+3322785698")
								.roles(roleList)
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
			driverService.initializeMissingStats();
		};
	}
}
