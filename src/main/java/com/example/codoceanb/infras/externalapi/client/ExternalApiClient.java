package com.example.codoceanb.infras.externalapi.client;

import com.example.codoceanb.infras.externalapi.dto.ExternalApiResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternalApiClient {

    private final RestTemplate restTemplate;

    @Value("${external.api.baseUrl}")
    private String baseUrl;

    public ExternalApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ExternalApiResponseDTO fetchExternalData(String endpoint) {
        String url = baseUrl + endpoint;
        return restTemplate.getForObject(url, ExternalApiResponseDTO.class);
    }
}
