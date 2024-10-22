package com.example.codoceanb.payment.controller;

import com.example.codoceanb.payment.dto.PaymentDTO;
import com.example.codoceanb.payment.service.PaymentService;
import com.example.codoceanb.payment.service.UpgradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    @Autowired
    private UpgradeService upgradeService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/execute")
    public ResponseEntity<String> paymentSuccess(@RequestHeader(value = "Authorization") String authHeader,
                                                 String token,
                                                 String PayerID) {
        return ResponseEntity.ok(upgradeService.executePayment(authHeader,token, PayerID));
    }

    @GetMapping("/")
    public ResponseEntity<List<PaymentDTO>> getPayments(@RequestHeader(value = "Authorization") String authHeader) {
        return ResponseEntity.ok(paymentService.getPayments(authHeader));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPayments(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(paymentService.getPayment(id));
    }
}
