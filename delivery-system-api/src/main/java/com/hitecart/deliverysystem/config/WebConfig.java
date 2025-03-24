package com.hitecart.deliverysystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebMvc
public class WebConfig {

@Value("${application.cors.origins:*}")
private List<String> allowedOrigins;
    @Bean
    public CorsFilter corsFilter(){
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        /**to pipline*/
        //config.setAllowCredentials(true);
        //config.setAllowedOrigins(allowedOrigins);
        //config.setAllowedHeaders(Arrays.asList("*"));//ORIGIN,CONTENT_TYPE,ACCEPT,AUTHORIZATION));
        //config.setAllowedMethods(Arrays.asList("*"));//"GET","POST","PATCH","DELETE","PUT"));
        config.setAllowCredentials(false);
        //config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedHeaders(Arrays.asList(
                HttpHeaders.ORIGIN,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT,
                HttpHeaders.AUTHORIZATION
        ));
        config.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "DELETE",
                "PUT",
                "PATCH"
        ));
        source.registerCorsConfiguration("/**",config);
        return new CorsFilter(source);
    }




    public WebMvcConfigurer corsConfig(){
        return  new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                System.out.println("http cors");
                WebMvcConfigurer.super.addCorsMappings(registry);
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods(
                                HttpMethod.GET.name(),
                                HttpMethod.POST.name(),
                                HttpMethod.PATCH.name(),
                                HttpMethod.DELETE.name())
                        .allowCredentials(true)
                        .allowedHeaders(HttpHeaders.CONTENT_TYPE,HttpHeaders.AUTHORIZATION);

            }
        };
    }

}
