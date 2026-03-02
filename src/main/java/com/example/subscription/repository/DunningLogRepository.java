package com.example.subscription.repository;

import com.example.subscription.entity.DunningLog;
import com.example.subscription.entity.Subscription;
import com.example.subscription.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DunningLogRepository extends JpaRepository<DunningLog, Long> {

    List<DunningLog> findBySubscriptionId(Long subscriptionId);
//    @Query("""
//        SELECT s FROM Subscription s
//        WHERE s.status = :status
//        AND s.nextRetryDate IS NOT NULL
//        AND s.nextRetryDate <= :today
//        AND s.renewalAttempts < :maxRetries
//    """)
//    List<Subscription> findSubscriptionsForRetry(
//            SubscriptionStatus status,
//            LocalDate today,
//            int maxRetries
//    );

}
