package com.example.subscription.DTO;

import com.example.subscription.enums.PaymentMethod;
import lombok.Data;

@Data
public class ChangePlanRequest {

    private Long newPlanId;
    private PaymentMethod method;
}