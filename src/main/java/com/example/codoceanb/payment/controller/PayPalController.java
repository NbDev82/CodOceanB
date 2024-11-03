package com.example.codoceanb.payment.controller;

import com.example.codoceanb.payment.request.UpgradeRequest;
import com.example.codoceanb.payment.service.UpgradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1/payment-paypal")
@RequiredArgsConstructor
public class PayPalController {

    @Autowired
    private UpgradeService upgradeService;

    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestBody UpgradeRequest request) {
        try {
            String linkPayout = upgradeService.processUpgradeRequest(request);
            return ResponseEntity.ok(linkPayout);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
