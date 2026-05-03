package com.fintech.fintech_backend.controller;

import com.fintech.fintech_backend.model.PaymentRequest;
import com.fintech.fintech_backend.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<Map<String, Object>> transfer(@RequestBody PaymentRequest request) {
        Map<String, Object> result = paymentService.processPayment(request);
        return ResponseEntity.ok(result);
    }
}