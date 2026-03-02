package com.example.subscription.service.impl;

import com.example.subscription.entity.Payment;
import com.example.subscription.enums.PaymentType;
import com.example.subscription.entity.Subscription;
import com.example.subscription.enums.PaymentMethod;
import com.example.subscription.enums.PaymentStatus;
import com.example.subscription.exception.PaymentFailedException;
import com.example.subscription.exception.ResourceNotFoundException;
import com.example.subscription.repository.PaymentRepository;
import com.example.subscription.repository.SubscriptionRepository;
import com.example.subscription.service.PaymentGatewaySimulator;
import com.example.subscription.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentGatewaySimulator gatewaySimulator;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Override
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Transactional
    public Payment processPayment(Long subscriptionId, BigDecimal amount,PaymentMethod method,PaymentType type) {

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

        boolean success = gatewaySimulator.process(amount);

        Payment payment = new Payment();
        payment.setSubscription(subscription);
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDate.now());
        payment.setPaymentStatus(success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        payment.setPaymentType(type);
        payment.setPaymentMethod(method);
        payment.setTransactionId("TXN-"+System.currentTimeMillis());

        if (!success) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new PaymentFailedException("Payment Failed...");
        }
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public Payment refundPayment(Payment payment, BigDecimal refundAmount) {
        payment.setRefundAmount(refundAmount);
        payment.setRefundDate(LocalDate.now());
        payment.setPaymentStatus(PaymentStatus.REFUNDED);

        return paymentRepository.save(payment);
    }


    @Override
    public Payment refundPaymentById(Long paymentId, String reason) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(()->new ResourceNotFoundException("Payment not found..."));

        if (payment.getPaymentStatus() != PaymentStatus.SUCCESS){
            throw new IllegalStateException("Only successful payment will be refunded");
        }

        payment.setRefundDate(LocalDate.now());
        payment.setRefundAmount(payment.getAmount());
        payment.setRefundReason(reason);
        payment.setPaymentStatus(PaymentStatus.REFUNDED);

        return paymentRepository.save(payment);

    }

    @Override
    public List<Payment> getPaymentsBySubscription(Long subscriptionId) {
        if (!subscriptionRepository.existsById(subscriptionId)){
            throw new ResourceNotFoundException("Subscription not found");
        }

        return paymentRepository.findBySubscriptionId(subscriptionId);
    }
}
