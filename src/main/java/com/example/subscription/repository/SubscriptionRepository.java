package com.example.subscription.repository;

import com.example.subscription.entity.Subscription;
import com.example.subscription.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {

    List<Subscription> findByUserId(Long userId);
    List<Subscription> findByStatus(Subscription status);

    @Query("""
    SELECT s FROM Subscription s
    JOIN FETCH s.plan
    WHERE s.status = :status
    AND s.autoRenew = true
    AND s.endDate <= :today
""")
    List<Subscription> findDueSubscriptions(
            @Param("status") SubscriptionStatus status,
            @Param("today") LocalDate today
    );
}
