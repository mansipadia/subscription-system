package com.example.subscription.unit;

import com.example.subscription.config.DunningConfig;
import com.example.subscription.entity.Plan;
import com.example.subscription.entity.Subscription;
import com.example.subscription.entity.User;
import com.example.subscription.enums.PaymentMethod;
import com.example.subscription.enums.PaymentStatus;
import com.example.subscription.enums.PaymentType;
import com.example.subscription.enums.SubscriptionStatus;
import com.example.subscription.repository.DunningLogRepository;
import com.example.subscription.repository.SubscriptionRepository;
import com.example.subscription.service.NotificationService;
import com.example.subscription.service.PaymentService;
import com.example.subscription.service.impl.DunningServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class DunningServiceTest {

    @InjectMocks
    private DunningServiceImpl dunningService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private DunningLogRepository dunningLogRepository;

    @Mock
    private DunningConfig config;

    private Subscription subscription;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1L);

        Plan plan = new Plan();
        plan.setPrice(BigDecimal.valueOf(100));
        plan.setDuration_days(30);

        subscription = new Subscription();
        subscription.setId(10L);
        subscription.setUser(user);
        subscription.setPlan(plan);
        subscription.setStatus(SubscriptionStatus.GRACE);
        subscription.setEndDate(LocalDate.now());
        subscription.setGraceEndDate(LocalDate.now().plusDays(5));
        subscription.setRenewalAttempts(0);
    }


    // SUCCESS RETRY
    @Test
    void shouldActivateSubscriptionOnSuccessfulRetry() {

        dunningService.retryPayment(subscription);

        verify(paymentService).processPayment(
                eq(10L),
                eq(BigDecimal.valueOf(100)),
                eq(PaymentMethod.CARD),
                eq(PaymentType.RENEWAL)
        );

        assertEquals(SubscriptionStatus.ACTIVE, subscription.getStatus());
        assertEquals(0, subscription.getRenewalAttempts());
        assertNull(subscription.getGraceEndDate());
        assertNull(subscription.getNextRetryDate());

        verify(dunningLogRepository).save(any());
    }

    // FAILED RETRY (UNDER LIMIT)
    @Test
    void shouldScheduleNextRetryWhenPaymentFails() {

        when(config.getMaxRetries()).thenReturn(3);
        when(config.getRetryIntervalsHours()).thenReturn(List.of(24, 48, 72));

        doThrow(new RuntimeException("Card declined"))
                .when(paymentService)
                .processPayment(anyLong(), any(), any(), any());

        dunningService.retryPayment(subscription);

        assertEquals(1, subscription.getRenewalAttempts());
        assertNotNull(subscription.getNextRetryDate());

        verify(notificationService).sendDunningNotification(
                any(),
                eq(1),
                eq(3),
                any()
        );

        verify(subscriptionRepository).save(subscription);
        verify(dunningLogRepository).save(any());
    }

    // FAILED RETRY (EXCEED LIMIT)
    @Test
    void shouldExpireWhenMaxRetriesReached() {

        subscription.setRenewalAttempts(2);

        when(config.getMaxRetries()).thenReturn(3);

        doThrow(new RuntimeException("Card declined"))
                .when(paymentService)
                .processPayment(anyLong(), any(), any(), any());

        dunningService.retryPayment(subscription);

        assertEquals(SubscriptionStatus.EXPIRED, subscription.getStatus());

        verify(notificationService).sendExpirationNotification(any());
        verify(dunningLogRepository).save(any());
    }

    // GRACE PERIOD EXPIRED
    @Test
    void shouldExpireIfGracePeriodOver() {

        subscription.setGraceEndDate(LocalDate.now().minusDays(1));

        dunningService.retryPayment(subscription);

        assertEquals(SubscriptionStatus.EXPIRED, subscription.getStatus());

        verify(notificationService).sendExpirationNotification(any());
    }

    // NOT IN GRACE
    @Test
    void shouldDoNothingIfNotInGrace() {

        subscription.setStatus(SubscriptionStatus.ACTIVE);

        dunningService.retryPayment(subscription);

        verifyNoInteractions(paymentService);
        verifyNoInteractions(dunningLogRepository);
    }

    // EXPIRE METHOD
    @Test
    void shouldExpireSubscription() {

        dunningService.expire(subscription);

        assertEquals(SubscriptionStatus.EXPIRED, subscription.getStatus());
        assertNull(subscription.getNextRetryDate());

        verify(notificationService).sendExpirationNotification(any());
    }

    // CREATE LOG
    @Test
    void shouldCreateDunningLog() {

        dunningService.createLog(
                subscription,
                1,
                PaymentStatus.FAILED,
                "Error",
                LocalDate.now()
        );

        verify(dunningLogRepository).save(any());
    }
}