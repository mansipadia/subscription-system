package com.example.subscription.repository;

import com.example.subscription.entity.Plan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class PlanRepositoryTest {

    @Autowired
    PlanRepository planRepository;

    @Test
    void shouldSaveAndRetrievePlan() {
        Plan plan = new Plan();
        plan.setName("Basic");
        plan.setPrice(BigDecimal.valueOf(2000));
        plan.setDuration_days(30);

        Plan saved = planRepository.save(plan);

        Optional<Plan> found = planRepository.findById(saved.getId());

        assertTrue(found.isPresent());
    }
}