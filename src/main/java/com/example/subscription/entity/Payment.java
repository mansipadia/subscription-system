package com.example.subscription.entity;

import com.example.subscription.enums.PaymentMethod;
import com.example.subscription.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @NotNull
    @Column(precision = 10,scale = 2)
    private BigDecimal amount;

    @Size(max = 30)
    @Enumerated(value = EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Size(max = 20)
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Size(max = 100)
    private String transactionId;

    @NotNull
    @CreationTimestamp
    private LocalDate paymentDate;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private PaymentType paymentType;

    @Column(precision = 10,scale = 2)
    private BigDecimal refundAmount;

    @Size(max = 255)
    private String refundReason;


    private LocalDate refundDate;








}
