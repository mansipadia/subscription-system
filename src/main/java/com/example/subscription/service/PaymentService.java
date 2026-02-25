package com.example.subscription.service;

import com.example.subscription.entity.Payment;
import com.example.subscription.entity.Subscription;
import com.example.subscription.enums.PaymentMethod;
import com.example.subscription.enums.PaymentType;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {

    Payment processPayment(Long subscriptionId, BigDecimal amount, PaymentMethod method,PaymentType type);
    Payment refundPayment(Payment payment,BigDecimal refundAmount);
    Payment refundPaymentById(Long paymentId, String reason);
    List<Payment> getPaymentsBySubscription(Long subscriptionId);
}
