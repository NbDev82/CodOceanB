package com.example.codoceanb.payment.service;

import com.example.codoceanb.payment.dto.PaymentDTO;
import com.example.codoceanb.payment.request.UpgradeRequest;

public interface UpgradeService {
    String processUpgradeRequest(UpgradeRequest upgradeRequest);
    String executePayment(String authHeader, String token, String payerId);
    PaymentDTO getInfoOrder(String token);
}
