package com.example.subscription.DTO;

import com.example.subscription.enums.CouponType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CouponResponse {

    private Long id;
    private String code;
    private BigDecimal discountPercentage;
    private BigDecimal discountAmount;
    private Integer usageLimit;
    private Integer usedCount=0;
    private Boolean active=true;
    private LocalDate expiryDate;
    private CouponType type;

}
