package com.example.subscription.DTO;

import com.example.subscription.enums.CouponType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CouponRequest {

    @NotBlank
    @Column(unique = true,nullable = false)
    private String code;

    @DecimalMin("0.0")
    @DecimalMax("100.00")
    @Column(precision = 5,scale = 2)
    private BigDecimal discountPercentage;


    @Column(precision = 10,scale = 2)
    private BigDecimal discountAmount;

    @NotNull
    private Integer usageLimit;

    private Integer usedCount=0;
    private Boolean active=true;

    @NotNull
    private LocalDate expiryDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CouponType type;

}
