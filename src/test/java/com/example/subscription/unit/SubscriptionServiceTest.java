package com.example.subscription.unit;

import com.example.subscription.DTO.SubscriptionResponse;
import com.example.subscription.entity.*;
import com.example.subscription.enums.*;
import com.example.subscription.exception.*;
import com.example.subscription.repository.*;
import com.example.subscription.service.PaymentService;

import com.example.subscription.service.impl.SubscriptionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PlanRepository planRepository;
    @Mock
    SubscriptionRepository subscriptionRepository;
    @Mock
    PaymentService paymentService;
    @Mock
    CouponRepository couponRepository;
    @Mock
    CouponUsageRepository couponUsageRepository;
    @Mock
    PaymentRepository paymentRepository;

    @InjectMocks
    SubscriptionServiceImpl service;

    @Test
    void shouldCreateSubscriptionWithoutCoupon() {

        User user = new User();
        user.setId(1L);

        Plan plan = new Plan();
        plan.setId(1L);
        plan.setPrice(BigDecimal.valueOf(100));
        plan.setDuration_days(30);

        Subscription savedSubscription = new Subscription();
        savedSubscription.setId(10L);
        savedSubscription.setUser(user);
        savedSubscription.setPlan(plan);
        savedSubscription.setFinalPrice(BigDecimal.valueOf(100));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(subscriptionRepository.saveAndFlush(any(Subscription.class)))
                .thenReturn(savedSubscription);

        SubscriptionResponse response =
                service.createSubscription(
                        1L,
                        1L,
                        null,
                        PaymentMethod.CARD,
                        PaymentType.SUBSCRIPTION
                );

        verify(paymentService).processPayment(
                eq(10L),
                eq(BigDecimal.valueOf(100)),
                eq(PaymentMethod.CARD),
                eq(PaymentType.SUBSCRIPTION)
        );

        assertEquals(SubscriptionStatus.ACTIVE, response.getStatus());
    }
}