package com.example.subscription.service.impl;

import com.example.subscription.config.DunningConfig;
import com.example.subscription.entity.DunningLog;
import com.example.subscription.entity.Subscription;
import com.example.subscription.enums.PaymentMethod;
import com.example.subscription.enums.PaymentStatus;
import com.example.subscription.enums.PaymentType;
import com.example.subscription.enums.SubscriptionStatus;
import com.example.subscription.exception.PaymentFailedException;
import com.example.subscription.repository.DunningLogRepository;
import com.example.subscription.repository.PaymentRepository;
import com.example.subscription.repository.SubscriptionRepository;
import com.example.subscription.service.DunningService;
import com.example.subscription.service.NotificationService;
import com.example.subscription.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Transactional(noRollbackFor = PaymentFailedException.class)
public class DunningServiceImpl implements DunningService {

    @Autowired
    PaymentService paymentService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    DunningLogRepository dunningLogRepository;

    @Autowired
    DunningConfig config;

    @Autowired
    PaymentRepository paymentRepository;

    @Override
    public void retryPayment(Subscription subscription) {

        boolean alreadyRenewed =
                paymentRepository.existsBySubscriptionIdAndPaymentTypeAndPaymentDate(
                        subscription.getId(),
                        PaymentType.RENEWAL,
                        LocalDate.now()
                );

        if (alreadyRenewed) {
            return;
        }

        if (subscription.getStatus() != SubscriptionStatus.GRACE){
            return;
        }

        if (subscription.getRenewalAttempts() >= config.getMaxRetries()) {
            return;
        }

        int currentAttempts = subscription.getRenewalAttempts();

        if(LocalDate.now().isAfter(subscription.getGraceEndDate())){
            expire(subscription);
            return;
        }

        try {

            paymentService.processPayment(subscription.getId(),subscription.getPlan().getPrice(), PaymentMethod.CARD, PaymentType.RENEWAL);

            subscription.setStatus(SubscriptionStatus.ACTIVE);
            LocalDate baseDate = subscription.getEndDate().isAfter(LocalDate.now())
                    ? subscription.getEndDate()
                    : LocalDate.now();

            subscription.setEndDate(
                    baseDate.plusDays(subscription.getPlan().getDuration_days())
            );
            subscription.setRenewalAttempts(0);
            subscription.setGraceEndDate(null);
            subscription.setNextRetryDate(null);

            createLog(subscription, currentAttempts+1,PaymentStatus.SUCCESS,null,null);
        }
        catch (Exception ex){

            int newAttempt = currentAttempts + 1;

            if (newAttempt >= config.getMaxRetries()){
                subscription.setRenewalAttempts(newAttempt);
                subscriptionRepository.save(subscription);
                expire(subscription);

                createLog(subscription, newAttempt,
                        PaymentStatus.FAILED,
                        ex.getMessage(),
                        null);
                return;
            }

            int interval = config.getRetryIntervalsHours().get(newAttempt - 1);

            LocalDate nextRetryDate =
                    LocalDate.now().plusDays(interval / 24);

            subscription.setRenewalAttempts(newAttempt);
            subscription.setNextRetryDate(nextRetryDate);

            subscriptionRepository.save(subscription);

            createLog(subscription,
                    newAttempt,
                    PaymentStatus.FAILED,
                    ex.getMessage(),
                    nextRetryDate);

            notificationService.sendDunningNotification(
                    subscription.getUser(),
                    newAttempt,
                    config.getMaxRetries(),
                    nextRetryDate
            );
        }
    }

    @Override
    public void expire(Subscription subscription) {
        subscription.setStatus(SubscriptionStatus.EXPIRED);
        subscription.setNextRetryDate(null);
        notificationService.sendExpirationNotification(subscription.getUser());
        subscriptionRepository.save(subscription);
    }

    @Override
    public void createLog(Subscription subscription, int attempt, PaymentStatus status, String reason, LocalDate nextRetry) {

        DunningLog log = new DunningLog();
        log.setSubscription(subscription);
        log.setAttemptNumber(attempt);
        log.setStatus(status);
        log.setAttemptedAt(LocalDateTime.now());
        log.setFailureReason(reason);
        log.setNextRetryDate(nextRetry);

        dunningLogRepository.save(log);
    }
}
