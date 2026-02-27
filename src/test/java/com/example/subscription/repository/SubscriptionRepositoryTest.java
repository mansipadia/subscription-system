package com.example.subscription.repository;

import com.example.subscription.entity.*;
import com.example.subscription.enums.PlanType;
import com.example.subscription.enums.SubscriptionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SubscriptionRepositoryTest {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldFindDueSubscriptions() {

        Plan plan = new Plan();
        plan.setName("Pro");
        plan.setDuration_days(20);
        plan.setPrice(BigDecimal.valueOf(1000));
        plan.setTier(PlanType.BASIC);

        User user = new User();
        user.setName("Rutvi");
        user.setEmail("rutvi@test.com");
        user.setPassword("123456");


        entityManager.persist(plan);
        entityManager.persist(user);

        Subscription subscription = new Subscription();
        subscription.setPlan(plan);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setAutoRenew(true);
        subscription.setEndDate(LocalDate.now().minusDays(1));
        subscription.setStartDate(LocalDate.now().plusDays(1));
        subscription.setUser(user);
        entityManager.persist(subscription);

        entityManager.flush();

        List<Subscription> result =
                subscriptionRepository.findDueSubscriptions(
                        SubscriptionStatus.ACTIVE,
                        LocalDate.now()
                );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPlan()).isNotNull(); // join fetch works
    }

    @Test
    void shouldFindSubscriptionsForRetry() {

        User user = new User();
        user.setName("mansi");
        user.setEmail("mansi@test.com");
        user.setPassword("123456");
        entityManager.persist(user);

        Plan plan = new Plan();
        plan.setName("Pro");
        plan.setDuration_days(20);
        plan.setPrice(BigDecimal.valueOf(1000));
        plan.setTier(PlanType.BASIC);
        entityManager.persist(plan);

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setStatus(SubscriptionStatus.GRACE);
        subscription.setNextRetryDate(LocalDate.now().minusDays(1));
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusDays(3));
        subscription.setPlan(plan);
        entityManager.persist(subscription);

        entityManager.flush();

        List<Subscription> result =
                subscriptionRepository.findSubscriptionsForRetry(
                        SubscriptionStatus.GRACE,
                        LocalDate.now()
                );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUser()).isNotNull(); // join fetch check
    }
}