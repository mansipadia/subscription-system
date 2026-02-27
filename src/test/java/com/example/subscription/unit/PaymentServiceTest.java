package com.example.subscription.unit;

import com.example.subscription.entity.Payment;
import com.example.subscription.entity.Subscription;
import com.example.subscription.enums.PaymentMethod;
import com.example.subscription.enums.PaymentStatus;
import com.example.subscription.enums.PaymentType;
import com.example.subscription.exception.PaymentFailedException;
import com.example.subscription.exception.ResourceNotFoundException;
import com.example.subscription.repository.PaymentRepository;
import com.example.subscription.repository.SubscriptionRepository;
import com.example.subscription.service.PaymentGatewaySimulator;
import com.example.subscription.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    PaymentGatewaySimulator gatewaySimulator;
    @Mock
    PaymentRepository paymentRepository;
    @Mock
    SubscriptionRepository subscriptionRepository;

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Test
    void shouldProcessPaymentSuccessfully() {

        Subscription sub = new Subscription();
        sub.setId(1L);

        when(subscriptionRepository.findById(1L))
                .thenReturn(Optional.of(sub));

        when(gatewaySimulator.process(any()))
                .thenReturn(true);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.processPayment(
                1L,
                BigDecimal.valueOf(1000),
                PaymentMethod.CARD,
                PaymentType.RENEWAL
        );

        assertEquals(PaymentStatus.SUCCESS, result.getPaymentStatus());
        assertEquals(BigDecimal.valueOf(1000), result.getAmount());
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void shouldThrowIfSubscriptionNotFound() {

        when(subscriptionRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                paymentService.processPayment(
                        1L,
                        BigDecimal.TEN,
                        PaymentMethod.CARD,
                        PaymentType.RENEWAL
                )
        );

        verifyNoInteractions(gatewaySimulator);
    }

    @Test
    void shouldThrowPaymentFailedExceptionIfGatewayFails() {

        Subscription sub = new Subscription();
        sub.setId(1L);

        when(subscriptionRepository.findById(1L))
                .thenReturn(Optional.of(sub));

        when(gatewaySimulator.process(any()))
                .thenReturn(false);

        when(paymentRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertThrows(PaymentFailedException.class, () ->
                paymentService.processPayment(
                        1L,
                        BigDecimal.valueOf(500),
                        PaymentMethod.CARD,
                        PaymentType.RENEWAL
                )
        );

        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void shouldRefundPayment() {

        Payment payment = new Payment();
        payment.setAmount(BigDecimal.valueOf(1000));

        when(paymentRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.refundPayment(
                payment,
                BigDecimal.valueOf(500)
        );

        assertEquals(PaymentStatus.REFUNDED, result.getPaymentStatus());
        assertEquals(BigDecimal.valueOf(500), result.getRefundAmount());
    }

    @Test
    void shouldRefundPaymentById() {

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setAmount(BigDecimal.valueOf(1000));
        payment.setPaymentStatus(PaymentStatus.SUCCESS);

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.of(payment));

        when(paymentRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.refundPaymentById(1L, "Customer request");

        assertEquals(PaymentStatus.REFUNDED, result.getPaymentStatus());
        assertEquals(BigDecimal.valueOf(1000), result.getRefundAmount());
        assertEquals("Customer request", result.getRefundReason());
    }

    @Test
    void shouldThrowIfPaymentNotFound() {

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                paymentService.refundPaymentById(1L, "reason")
        );
    }

    @Test
    void shouldThrowIfPaymentNotSuccessful() {

        Payment payment = new Payment();
        payment.setPaymentStatus(PaymentStatus.FAILED);

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.of(payment));

        assertThrows(IllegalStateException.class, () ->
                paymentService.refundPaymentById(1L, "reason")
        );
    }

    @Test
    void shouldReturnPaymentsForSubscription() {

        when(subscriptionRepository.existsById(1L))
                .thenReturn(true);

        when(paymentRepository.findBySubscriptionId(1L))
                .thenReturn(List.of(new Payment()));

        List<Payment> payments =
                paymentService.getPaymentsBySubscription(1L);

        assertEquals(1, payments.size());
    }
}