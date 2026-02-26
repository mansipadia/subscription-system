package com.example.subscription.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SubscriptionAddOnResponse {

    private Long id;
    private Long subscriptionId;
    private Long addOnId;
    private String addOnName;
    private Integer unitsIncluded;
    private Integer unitsUsed;
    private LocalDate billingCycleStart;
    private LocalDate billingCycleEnd;
}