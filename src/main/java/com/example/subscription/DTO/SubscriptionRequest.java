package com.example.subscription.DTO;

import com.example.subscription.enums.PaymentMethod;
import com.example.subscription.enums.PaymentType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long planId;

    private String couponCode;

    @NotNull
    private PaymentMethod method;

    @NotNull
    private PaymentType type;
}
