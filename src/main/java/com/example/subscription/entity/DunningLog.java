package com.example.subscription.entity;

import com.example.subscription.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class DunningLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id",nullable = false)
    private Subscription subscription;

    @NotNull
    private Integer attemptNumber;

    @NotNull
    private LocalDateTime attemptedAt;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PaymentStatus status;

    private String failureReason;

    private LocalDate nextRetryDate;
}
