package com.example.subscription.service.impl;

import com.example.subscription.entity.User;
import com.example.subscription.service.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Override
    public void sendDunningNotification(User user, int attempts, int max, LocalDate nextRetry) {
        System.out.println("Dunning Email : " +user.getEmail()+ ", attempt "+attempts+
                " of " + max + ", next retry "+nextRetry);
    }

    @Override
    public void sendExpirationNotification(User user) {
        System.out.println("Subscription expired for : "+user.getEmail());
    }
}
