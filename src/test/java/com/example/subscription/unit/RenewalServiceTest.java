package com.example.subscription.unit;

import com.example.subscription.config.DunningConfig;
import com.example.subscription.entity.DunningLog;
import com.example.subscription.entity.Payment;
import com.example.subscription.entity.Plan;
import com.example.subscription.entity.Subscription;
import com.example.subscription.enums.PaymentStatus;
import com.example.subscription.enums.SubscriptionStatus;
import com.example.subscription.repository.DunningLogRepository;
import com.example.subscription.repository.PaymentRepository;
import com.example.subscription.repository.SubscriptionRepository;
import com.example.subscription.service.NotificationService;
import com.example.subscription.service.PaymentService;
import com.example.subscription.service.impl.RenewalServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RenewalServiceImplTest {

    @Mock
    PaymentService paymentService;
    @Mock
    PaymentRepository paymentRepository;
    @Mock
    SubscriptionRepository subscriptionRepository;
    @Mock
    DunningConfig config;
    @Mock
    DunningLogRepository dunningLogRepository;
    @Mock
    NotificationService notificationService;

    @InjectMocks
    RenewalServiceImpl renewalService;

    @Test
    void shouldSkipIfSubscriptionNotActive() {

        Subscription sub = new Subscription();
        sub.setStatus(SubscriptionStatus.GRACE);

        renewalService.processRenewal(sub);


        verifyNoInteractions(paymentService); //no interaction
        verifyNoInteractions(subscriptionRepository);
    }

    @Test
    void shouldRenewSuccessfully() {

        Subscription sub = buildActiveSubscription();

        when(paymentRepository.existsBySubscriptionIdAndPaymentTypeAndPaymentDate(
                anyLong(), any(), any()
        )).thenReturn(false);

        when(paymentService.processPayment(
                anyLong(), any(), any(), any()
        )).thenReturn(new Payment());

        renewalService.processRenewal(sub);

        assertEquals(SubscriptionStatus.ACTIVE, sub.getStatus());
        verify(subscriptionRepository).save(sub);
        verifyNoInteractions(dunningLogRepository);
    }

    @Test
    void shouldExtendEndDateOnSuccess() {

        Subscription sub = buildActiveSubscription();
        LocalDate originalEnd = sub.getEndDate();

        when(paymentRepository.existsBySubscriptionIdAndPaymentTypeAndPaymentDate(
                anyLong(), any(), any()
        )).thenReturn(false);

        when(paymentService.processPayment(
                anyLong(), any(), any(), any()
        )).thenReturn(new Payment());

        renewalService.processRenewal(sub);

        assertEquals(
                originalEnd.plusDays(sub.getPlan().getDuration_days()),
                sub.getEndDate()
        );
    }

    @Test
    void shouldCreateCorrectDunningLog() {

        Subscription sub = buildActiveSubscription();

        when(paymentRepository.existsBySubscriptionIdAndPaymentTypeAndPaymentDate(
                anyLong(), any(), any()
        )).thenReturn(false);

        when(paymentService.processPayment(
                anyLong(), any(), any(), any()
        )).thenThrow(new RuntimeException("Fail"));

        when(config.getGracePeriodDays()).thenReturn(3);
        when(config.getRetryIntervalsHours()).thenReturn(List.of(24));
        when(config.getMaxRetries()).thenReturn(3);

        renewalService.processRenewal(sub);

        ArgumentCaptor<DunningLog> captor =
                ArgumentCaptor.forClass(DunningLog.class);

        verify(dunningLogRepository).save(captor.capture());

        DunningLog log = captor.getValue();

        assertEquals(1, log.getAttemptNumber());
        assertEquals(PaymentStatus.FAILED, log.getStatus());
    }
    private Subscription buildActiveSubscription() {

        Plan plan = new Plan();
        plan.setPrice(BigDecimal.valueOf(1000));
        plan.setDuration_days(30);

        Subscription sub = new Subscription();
        sub.setId(1L);
        sub.setStatus(SubscriptionStatus.ACTIVE);
        sub.setEndDate(LocalDate.now());
        sub.setPlan(plan);

        return sub;
    }
}