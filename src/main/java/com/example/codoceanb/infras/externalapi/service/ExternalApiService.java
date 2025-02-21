package com.example.codoceanb.infras.externalapi.service;

import com.example.codoceanb.infras.externalapi.dto.ExternalApiResponseDTO;

public interface ExternalApiService {
    ExternalApiResponseDTO getExternalData(String endpoint);
}
