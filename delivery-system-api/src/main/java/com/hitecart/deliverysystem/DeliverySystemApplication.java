package com.hitecart.deliverysystem;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableCaching
@RequiredArgsConstructor
public class DeliverySystemApplication {


	public static void main(String[] args) {
		SpringApplication.run(DeliverySystemApplication.class, args);
	}

//
//	@Bean
//	public CommandLineRunner runner() {
//		return args -> {//CodeGeneratorExecutor.executeCodeGenerator();
//
//			Role userRole = roleRepository.findByName(RoleType.ROLE_USER).orElseGet(()->{
//				return roleRepository.save( Role.builder()
//								 .name(RoleType.ROLE_USER)
//								 .createdBy(0)
//								 .build());}
//			);
//
//
//			if(dpcRepository.findAll().isEmpty()){
//				dpcRepository.save(
//						DeliveryPointCategory.builder()
//								.name("Standard-Range")
//								.description("Companies in this category offer basic, cost-effective delivery services. Their pricing reflects standard shipping costs without extra premiums.")
//								.createdBy(0)
//								.additionalFees(0.0)
//								.build()
//				);
//				dpcRepository.save(
//						DeliveryPointCategory.builder().name("Mid-Range")
//								.description("These companies balance affordability with enhanced security measures. They keep delivery costs moderate while implementing additional protocols to ensure safer transit.")
//								.createdBy(0)
//								.additionalFees(10.0)
//								.build()
//				);
//				dpcRepository.save(
//						DeliveryPointCategory.builder().name("Hight-End")
//								.description("Serving high-value goods, these companies charge premium delivery fees. Their services include advanced, secure logistics and specialized handling to match the elevated value of the items.")
//								.createdBy(0)
//								.additionalFees(20.0)
//								.build()
//				);
//			}
//			driverService.initializeMissingStats();
//		};
//	}
}
