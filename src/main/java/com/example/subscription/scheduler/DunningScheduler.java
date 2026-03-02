package com.example.subscription.scheduler;

import com.example.subscription.config.DunningConfig;
import com.example.subscription.entity.Subscription;
import com.example.subscription.enums.SubscriptionStatus;
import com.example.subscription.repository.DunningLogRepository;
import com.example.subscription.repository.SubscriptionRepository;
import com.example.subscription.service.DunningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DunningScheduler {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    DunningService dunningService;

    @Autowired
    DunningLogRepository dunningLogRepository;

    @Autowired
    DunningConfig config;

    @Scheduled(fixedDelayString = "${dunning.scheduler.interval-ms}")
    public void runDunning(){

        List<Subscription> subscriptions =
                dunningLogRepository.findSubscriptionsForRetry(
                        SubscriptionStatus.GRACE,
                        LocalDate.now(),
                        config.getMaxRetries()
                );

        for (Subscription sub: subscriptions){
            dunningService.retryPayment(sub);
        }
    }
}
