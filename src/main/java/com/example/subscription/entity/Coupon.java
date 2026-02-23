package com.example.subscription.entity;

import com.example.subscription.enums.CouponType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
