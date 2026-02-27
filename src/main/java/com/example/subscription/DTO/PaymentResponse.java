package com.example.subscription.DTO;

import com.example.subscription.enums.PaymentType;
import com.example.subscription.enums.PaymentMethod;
import com.example.subscription.enums.PaymentStatus;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentResponse {

    private Long id;

    private Long subscriptionId;

    private BigDecimal amount;

    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;

    private String transactionId;

    private LocalDate paymentDate;

    private PaymentType paymentType;

    private BigDecimal refundAmount;

    private String refundReason;

    private LocalDate refundDate;
}
