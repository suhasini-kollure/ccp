package com.ccp.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cards and Payment API")
                        .description("Spring boot project for cards and payment system")
                        .version("0.0.1-SNAPSHOT")
                        .contact(new Contact().name("Suhasini Kollure").email("kolluresuhasinik@gmail.com")));
    }
}
