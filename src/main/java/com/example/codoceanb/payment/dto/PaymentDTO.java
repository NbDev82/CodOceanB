package com.example.codoceanb.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PaymentDTO {
    private String id;
    private String paymentMethod;
    private String paymentStatus;
    private String paymentDate;
    private String amount;
    private String currency;
    private String description;
}
