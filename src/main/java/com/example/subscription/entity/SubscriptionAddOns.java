package com.example.subscription.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(
        name = "subscription_add_ons",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {
                        "subscription_id",
                        "add_on_id",
                        "billing_cycle_start"
                }
        )
)
public class SubscriptionAddOns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "add_on_id")
    private AddOns addOns;

    private Integer unitsUsed=0;

    private Integer unitsIncluded=0;

    @NotNull
    private LocalDate billingCycleStart;

    @NotNull
    private LocalDate billingCycleEnd;


}
