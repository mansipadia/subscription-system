package com.example.subscription.service;

import com.example.subscription.entity.Subscription;

public interface RenewalService {
    void processRenewal(Subscription subscription);
}
