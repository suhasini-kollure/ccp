package com.ccp.configuration;

import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InfoContributor infoContributor() {
        return builder -> builder
                .withDetail("application",
                        Map.of("name", "Cards and Payment System",
                                "description", "Spring boot project for cards and payment system",
                                "version", "0.0.1-SNAPSHOT"))
                .build();
    }
}


