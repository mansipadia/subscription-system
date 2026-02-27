package com.example.subscription.repository;

import com.example.subscription.entity.*;
import com.example.subscription.enums.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldFindPaymentsBySubscriptionId() {

        Subscription subscription = createSubscription();
        Payment p1 = createPayment(subscription, PaymentStatus.SUCCESS, LocalDate.now());
        Payment p2 = createPayment(subscription, PaymentStatus.FAILED, LocalDate.now().minusDays(1));

        entityManager.flush();

        List<Payment> result =
                repository.findBySubscriptionId(subscription.getId());

        assertThat(result).hasSize(2);
    }


    @Test
    void shouldCheckIfPaymentExists() {

        Subscription subscription = createSubscription();

        LocalDate today = LocalDate.now();

        createPayment(subscription, PaymentStatus.SUCCESS, today);

        entityManager.flush();

        Boolean exists = repository.existsBySubscriptionIdAndPaymentTypeAndPaymentDate(
                subscription.getId(),
                PaymentType.RENEWAL,
                today
        );

        assertThat(exists).isTrue();
    }

    private Subscription createSubscription() {

        User user = new User();
        user.setName("Test");
        user.setEmail("test@test.com");
        user.setPassword("123");
        entityManager.persist(user);

        Plan plan = new Plan();
        plan.setName("Basic");
        plan.setDuration_days(30);
        plan.setPrice(BigDecimal.valueOf(500));
        plan.setTier(PlanType.BASIC);
        entityManager.persist(plan);

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setPlan(plan);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusDays(30));

        return entityManager.persist(subscription);
    }

    private Payment createPayment(
            Subscription subscription,
            PaymentStatus status,
            LocalDate paymentDate) {

        Payment payment = new Payment();
        payment.setSubscription(subscription);
        payment.setAmount(BigDecimal.valueOf(1000));
        payment.setPaymentStatus(status);
        payment.setPaymentType(PaymentType.RENEWAL);
        payment.setPaymentDate(paymentDate);
        payment.setPaymentMethod(PaymentMethod.CARD);

        return entityManager.persist(payment);
    }
}