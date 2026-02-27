package com.example.subscription.repository;

import com.example.subscription.entity.Subscription;
import com.example.subscription.entity.SubscriptionAddOns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionAddOnRepository extends JpaRepository<SubscriptionAddOns,Long> {

    List<SubscriptionAddOns> findBySubscription(Subscription subscription);

    Optional<SubscriptionAddOns> findBySubscription_IdAndAddOns_IdAndBillingCycleStart(Long subscriptionId, Long addOnId, LocalDate billingCycleStart);

    boolean existsBySubscription_IdAndAddOns_IdAndBillingCycleStart(
            Long subscriptionId,
            Long addOnId,
            LocalDate billingCycleStart
    );

}
