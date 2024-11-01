package com.hitecart.deliverysystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                contact = @io.swagger.v3.oas.annotations.info.Contact(
                        name = "Ahmed",
                        email = "contact@ahmedconsulting.com",
                        url = "https://ahmedconsulting.com/course"
                ),
                description = "OpenApi documentation for Spring Security",
                title = "OpenApi specification - Ahmed",
                version = "1.0",
                license = @io.swagger.v3.oas.annotations.info.License(
                        name = "My Licence Name",
                        url = "https://mylicenceurl.com"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080/api/v1"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "http://34.118.107.30:8080/api/v1"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiSwaggerConfiguration {
    /*
    @Bean
    public OpenAPI baseOpenApi(){
        return new OpenAPI()
                .info( new Info()
                        .contact(new Contact()
                                .email("hitecart.tn@gmail.com")
                                .name("Hitec Art")
                                .url("https://hitecart.com")
                        )
                        .description("Delivery-System REST Api documantation")
                        .title("Delivery-System REST API ")
                        .version("1.0.0")
                        .license(new License().name("Licence Name")
                                .url("https://sonelicence.com"))
                        .termsOfService("Terme Of Service")

                );
    }*/
}
