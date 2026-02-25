package com.example.subscription.repository;

import com.example.subscription.entity.Payment;
import com.example.subscription.enums.PaymentStatus;
import com.example.subscription.enums.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findBySubscriptionId(Long subscriptionId);
    List<Payment> findByPaymentStatus(PaymentStatus status);

    Optional<Payment> findTopBySubscriptionIdOrderByPaymentDateDesc(Long subscriptionId);
    Optional<Payment> findTopBySubscriptionIdAndPaymentStatusOrderByPaymentDateDesc(Long subscriptionId,PaymentStatus status);
    Boolean existsBySubscriptionIdAndPaymentTypeAndPaymentDate(Long subscriptionId, PaymentType type, LocalDate paymentDate);
}
