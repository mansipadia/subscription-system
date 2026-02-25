package com.example.subscription.DTO;

import com.example.subscription.enums.PaymentMethod;
import com.example.subscription.enums.PaymentType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {

    private Long subscriptionId;
    private BigDecimal amount;
    private PaymentMethod method;
    private PaymentType type;
}
