package com.fintech.fintech_backend.service;

import com.fintech.fintech_backend.model.PaymentRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    public Map<String, Object> processPayment(PaymentRequest request) {
        // Simulating payment processing
        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("transactionId", "TXN-" + System.currentTimeMillis());
        response.put("fromAccount", request.getFromAccountId());
        response.put("toAccount", request.getToAccountId());
        response.put("amount", request.getAmount());
        response.put("currency", request.getCurrency());
        return response;
    }
}