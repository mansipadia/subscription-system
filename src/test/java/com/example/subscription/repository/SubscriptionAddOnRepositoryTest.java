package com.example.subscription.repository;

import com.example.subscription.entity.*;
import com.example.subscription.enums.PlanType;
import com.example.subscription.enums.SubscriptionStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

@DataJpaTest
class SubscriptionAddOnRepositoryTest {

    @Autowired
    private SubscriptionAddOnRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldFindAddOnsBySubscription() {

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

        Subscription subscription1 = new Subscription();
        subscription1.setUser(user);
        subscription1.setStatus(SubscriptionStatus.GRACE);
        subscription1.setNextRetryDate(LocalDate.now().minusDays(1));
        subscription1.setStartDate(LocalDate.now());
        subscription1.setEndDate(LocalDate.now().plusDays(3));
        subscription1.setPlan(plan);
        entityManager.persist(subscription1);

        Subscription subscription2 = new Subscription();
        subscription2.setUser(user);
        subscription2.setStatus(SubscriptionStatus.GRACE);
        subscription2.setNextRetryDate(LocalDate.now().minusDays(1));
        subscription2.setStartDate(LocalDate.now());
        subscription2.setEndDate(LocalDate.now().plusDays(3));
        subscription2.setPlan(plan);
        entityManager.persist(subscription2);

        AddOns addOns = new AddOns();
        addOns.setName("Extra Storage");
        addOns.setUnitName("unit name");
        addOns.setUnitPrice(BigDecimal.valueOf(100));
        entityManager.persist(addOns);

        SubscriptionAddOns a1 = new SubscriptionAddOns();
        a1.setSubscription(subscription1);
        a1.setAddOns(addOns);
        a1.setBillingCycleStart(LocalDate.now());
        a1.setBillingCycleEnd(LocalDate.now().plusDays(5));
        entityManager.persist(a1);

        SubscriptionAddOns a2 = new SubscriptionAddOns();
        a2.setSubscription(subscription2);
        a2.setAddOns(addOns);
        a2.setBillingCycleStart(LocalDate.now());
        a2.setBillingCycleEnd(LocalDate.now().plusDays(5));
        entityManager.persist(a2);

        entityManager.flush();

        List<SubscriptionAddOns> result =
                repository.findBySubscription(subscription1);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSubscription().getId())
                .isEqualTo(subscription1.getId());
    }
}