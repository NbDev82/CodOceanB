package com.example.codoceanb.payment.controller;

import com.example.codoceanb.payment.dto.PaymentDTO;
import com.example.codoceanb.payment.service.UpgradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment-info")
@RequiredArgsConstructor
public class OrdersController {

    @Autowired
    private UpgradeService upgradeService;

    @GetMapping("/orders/{id}")
    public ResponseEntity<PaymentDTO> getInfoOrder(@PathVariable(name = "id") String orderId) {
        return ResponseEntity.ok(upgradeService.getInfoOrder(orderId));
    }
}
