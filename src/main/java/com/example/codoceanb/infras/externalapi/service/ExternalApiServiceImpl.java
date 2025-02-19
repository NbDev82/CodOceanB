package com.example.codoceanb.infras.externalapi.service;

import com.example.codoceanb.infras.externalapi.client.ExternalApiClient;
import com.example.codoceanb.infras.externalapi.dto.ExternalApiResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExternalApiServiceImpl implements ExternalApiService{

    @Autowired
    private ExternalApiClient externalApiClient;

    public ExternalApiResponseDTO getExternalData(String endpoint) {
        return externalApiClient.fetchExternalData(endpoint);
    }
}
