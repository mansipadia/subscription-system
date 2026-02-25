package com.example.subscription.scheduler;

import com.example.subscription.entity.Subscription;
import com.example.subscription.enums.SubscriptionStatus;
import com.example.subscription.repository.SubscriptionRepository;
import com.example.subscription.service.RenewalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@ConditionalOnProperty(name = "renewal.scheduler.enabled",havingValue = "true",matchIfMissing = true)
public class RenewalScheduler {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private RenewalService renewalService;

    @Scheduled(fixedRateString = "${renewal.scheduler.interval-ms}")
    public void runRenewals(){
        List<Subscription> dueSubscription = subscriptionRepository.findDueSubscriptions(
                        SubscriptionStatus.ACTIVE,
                        LocalDate.now()
                );

        for (Subscription subscription:dueSubscription){
            renewalService.processRenewal(subscription);
        }

    }


}
