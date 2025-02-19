package com.example.codoceanb.infras.externalapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ExternalApiConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
