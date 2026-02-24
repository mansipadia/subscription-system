package com.example.subscription.DTO;

import com.example.subscription.enums.PlanType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdatePlanRequest {

    private String name;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal price;

    @Min(1)
    private Integer durationDays;

    private String description;

    private PlanType tier;

    private Boolean active;
}