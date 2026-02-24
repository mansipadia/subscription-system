package com.example.subscription.DTO;

import com.example.subscription.enums.PlanType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlanResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer durationDays;
    private String description;
    private PlanType tier;
    private Boolean active;
}
