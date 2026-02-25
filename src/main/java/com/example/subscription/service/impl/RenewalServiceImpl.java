package com.example.subscription.service.impl;

import com.example.subscription.entity.Payment;
import com.example.subscription.entity.Subscription;
import com.example.subscription.enums.PaymentMethod;
import com.example.subscription.enums.PaymentType;
import com.example.subscription.enums.SubscriptionStatus;
import com.example.subscription.repository.PaymentRepository;
import com.example.subscription.repository.SubscriptionRepository;
import com.example.subscription.service.PaymentService;
import com.example.subscription.service.RenewalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Transactional
public class RenewalServiceImpl implements RenewalService {

    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Override
    public void processRenewal(Subscription subscription) {

        if (subscription.getStatus() != SubscriptionStatus.ACTIVE){
            return;
        }

        boolean alreadyRenewed = paymentRepository.existsBySubscriptionIdAndPaymentTypeAndPaymentDate(subscription.getId(), PaymentType.RENEWAL, LocalDate.now());

        if(alreadyRenewed){
            return;
        }

        BigDecimal amount = subscription.getPlan().getPrice();

        try{

            Payment payment = paymentService.processPayment(subscription.getId(), amount, PaymentMethod.CARD,PaymentType.RENEWAL);

            subscription.setEndDate(subscription.getEndDate().plusDays(subscription.getPlan().getDuration_days()));

            subscription.setStatus(SubscriptionStatus.ACTIVE);

            subscriptionRepository.save(subscription);

        }
        catch (Exception e){

            subscription.setStatus(SubscriptionStatus.GRACE);
            subscription.setGraceEndDate(LocalDate.now().plusDays(7));
            subscription.setNextRetryDate(LocalDate.now().plusDays(2));
            subscription.setRenewalAttempts(subscription.getRenewalAttempts() + 1);

            subscriptionRepository.save(subscription);

        }

    }
}
