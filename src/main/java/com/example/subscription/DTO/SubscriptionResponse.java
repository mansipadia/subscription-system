package com.example.subscription.DTO;

import com.example.subscription.enums.SubscriptionStatus;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SubscriptionResponse {

    private Long id;
    private Long user_id;
    private Long plan_id;
    private LocalDate startDate;
    private LocalDate endDate;
    private SubscriptionStatus status;
    private BigDecimal finalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
