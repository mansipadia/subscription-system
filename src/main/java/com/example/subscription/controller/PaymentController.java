package com.example.subscription.controller;

import com.example.subscription.DTO.PaymentRequest;
import com.example.subscription.DTO.RefundRequest;
import com.example.subscription.entity.Payment;
import com.example.subscription.entity.Subscription;
import com.example.subscription.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> processPayment(
            @RequestBody PaymentRequest request) {


        Payment payment = paymentService.processPayment(
                request.getSubscriptionId(),
                request.getAmount(),
                request.getMethod(),
                request.getType()
        );

        return ResponseEntity.status(201).body(payment);
    }

    @PutMapping("/{id}/refund")
    public ResponseEntity<Payment> refundPayment(
            @PathVariable Long id,
            @RequestBody RefundRequest request) {

        return ResponseEntity.ok(
                paymentService.refundPaymentById(id, request.getRefundReason())
        );
    }

    @GetMapping("/subscription/{subId}")
    public ResponseEntity<List<Payment>> getPaymentsBySubscription(
            @PathVariable Long subId) {

        return ResponseEntity.ok(
                paymentService.getPaymentsBySubscription(subId)
        );
    }
}
