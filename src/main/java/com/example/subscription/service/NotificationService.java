package com.example.subscription.service;

import com.example.subscription.entity.User;

import java.time.LocalDate;

public interface NotificationService {

    void sendDunningNotification(User user, int attempts, int max, LocalDate nextRetry);
    void sendExpirationNotification(User user);

}
