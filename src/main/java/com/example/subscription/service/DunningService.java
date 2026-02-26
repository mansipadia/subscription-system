package com.example.subscription.service;

import com.example.subscription.entity.Subscription;
import com.example.subscription.enums.PaymentStatus;

import java.time.LocalDate;

public interface DunningService {

    void retryPayment(Subscription subscription);
    void expire(Subscription subscription);
    void createLog(Subscription subscription, int attempt, PaymentStatus status, String reason, LocalDate nextRetry);
}
