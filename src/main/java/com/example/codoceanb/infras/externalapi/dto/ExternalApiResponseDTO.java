package com.example.codoceanb.infras.externalapi.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ExternalApiResponseDTO {
    private String data;
    private String status;
}
