package com.example.subscription.service.impl;

import com.example.subscription.config.DunningConfig;
import com.example.subscription.entity.DunningLog;
import com.example.subscription.entity.Payment;
import com.example.subscription.entity.Subscription;
import com.example.subscription.enums.PaymentMethod;
import com.example.subscription.enums.PaymentStatus;
import com.example.subscription.enums.PaymentType;
import com.example.subscription.enums.SubscriptionStatus;
import com.example.subscription.repository.DunningLogRepository;
import com.example.subscription.repository.PaymentRepository;
import com.example.subscription.repository.SubscriptionRepository;
import com.example.subscription.service.NotificationService;
import com.example.subscription.service.PaymentService;
import com.example.subscription.service.RenewalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Transactional
public class RenewalServiceImpl implements RenewalService {

    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    DunningConfig config;

    @Autowired
    DunningLogRepository dunningLogRepository;

    @Autowired
    NotificationService notificationService;

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
            subscription.setGraceEndDate(LocalDate.now().plusDays(config.getGracePeriodDays()));

            subscription.setRenewalAttempts(1);

            int firstInterval = config.getRetryIntervalsHours().get(0);

            LocalDate nextRetry = LocalDate.now().plusDays(firstInterval / 24);
            subscription.setNextRetryDate(nextRetry);

            DunningLog log = new DunningLog();

            log.setSubscription(subscription);
            log.setAttemptNumber(1);
            log.setAttemptedAt(LocalDateTime.now());
            log.setStatus(PaymentStatus.FAILED);
            log.setFailureReason(e.getMessage());
            log.setNextRetryDate(nextRetry);

            dunningLogRepository.save(log);

            notificationService.sendDunningNotification(subscription.getUser(),1,config.getMaxRetries(),nextRetry);

            subscriptionRepository.save(subscription);

        }

    }
}
