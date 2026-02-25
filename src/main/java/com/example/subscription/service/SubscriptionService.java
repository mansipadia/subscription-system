package com.example.subscription.service;

import com.example.subscription.DTO.SubscriptionResponse;
import com.example.subscription.enums.PaymentMethod;
import com.example.subscription.enums.PaymentType;

import java.util.List;


public interface SubscriptionService {

    SubscriptionResponse createSubscription(Long userId, Long planId, String couponCode, PaymentMethod method, PaymentType type);
    SubscriptionResponse cancelSubscription(Long subscriptionId);
    SubscriptionResponse changePlan(Long subscriptionId,Long newPlanId,PaymentMethod method);
    SubscriptionResponse getSubscriptionById(Long subscriptionId);
    List<SubscriptionResponse> getAllSubscriptions();

}
