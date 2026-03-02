package com.example.subscription.service;

import com.example.subscription.DTO.AddOnBillingResponse;
import com.example.subscription.DTO.AttachAddOnRequest;
import com.example.subscription.DTO.SubscriptionAddOnResponse;
import com.example.subscription.DTO.UsageRequest;
import com.example.subscription.entity.SubscriptionAddOns;

import java.math.BigDecimal;
import java.util.List;

public interface SubscriptionAddOnService {

    SubscriptionAddOnResponse attachAddOn(Long subscriptionId, AttachAddOnRequest request);

    SubscriptionAddOnResponse recordUsage(Long subscriptionAddOnId, Long addOnId ,UsageRequest request);

    List<SubscriptionAddOns> getSubscriptionAddOns(Long subscriptionId);

    AddOnBillingResponse calculateCurrentCycleCharges(Long subscriptionId);

}
