package com.example.codoceanb.payment.service;

import com.example.codoceanb.payment.dto.PaymentDTO;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
    List<PaymentDTO> getPayments(String authHeader);

    PaymentDTO getPayment(UUID id);
}
