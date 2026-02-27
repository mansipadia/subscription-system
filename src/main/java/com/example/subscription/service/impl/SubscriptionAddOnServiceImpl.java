package com.example.subscription.service.impl;

import com.example.subscription.DTO.AttachAddOnRequest;
import com.example.subscription.DTO.SubscriptionAddOnResponse;
import com.example.subscription.DTO.UsageRequest;
import com.example.subscription.entity.AddOns;
import com.example.subscription.entity.Subscription;
import com.example.subscription.entity.SubscriptionAddOns;
import com.example.subscription.exception.ResourceNotFoundException;
import com.example.subscription.repository.AddOnRepository;
import com.example.subscription.repository.SubscriptionAddOnRepository;
import com.example.subscription.repository.SubscriptionRepository;
import com.example.subscription.service.SubscriptionAddOnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubscriptionAddOnServiceImpl implements SubscriptionAddOnService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    SubscriptionAddOnRepository subscriptionAddOnRepository;

    @Autowired
    AddOnRepository addOnRepository;


    @Override
    public SubscriptionAddOnResponse attachAddOn(Long subscriptionId,AttachAddOnRequest request) {

        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(()-> new ResourceNotFoundException("Subscription not found"));

        AddOns addOns = addOnRepository.findById(request.getAddOnId()).orElseThrow(()->new ResourceNotFoundException("Add-Ons not found"));

        LocalDate startDate = subscription.getStartDate();
        LocalDate endDate = subscription.getEndDate();

        SubscriptionAddOns subscriptionAddOns = new SubscriptionAddOns();
        subscriptionAddOns.setSubscription(subscription);
        subscriptionAddOns.setAddOns(addOns);
        subscriptionAddOns.setUnitsIncluded(request.getUnitsIncluded());
        subscriptionAddOns.setUnitsUsed(0);
        subscriptionAddOns.setBillingCycleStart(startDate);
        subscriptionAddOns.setBillingCycleEnd(endDate);

        if (subscriptionAddOnRepository
                .existsBySubscription_IdAndAddOns_IdAndBillingCycleStart(
                        subscriptionId,
                        request.getAddOnId(),
                        startDate
                )) {
            throw new IllegalStateException("Add-on already attached for this cycle");
        }
        SubscriptionAddOns saved = subscriptionAddOnRepository.save(subscriptionAddOns);

        // Convert to DTO
        SubscriptionAddOnResponse response = new SubscriptionAddOnResponse();
        response.setId(saved.getId());
        response.setSubscriptionId(subscription.getId());
        response.setAddOnId(addOns.getId());
        response.setAddOnName(addOns.getName());
        response.setUnitsIncluded(saved.getUnitsIncluded());
        response.setUnitsUsed(saved.getUnitsUsed());
        response.setBillingCycleStart(saved.getBillingCycleStart());
        response.setBillingCycleEnd(saved.getBillingCycleEnd());

        return response;
    }

    @Override
    public SubscriptionAddOnResponse recordUsage(Long subscriptionId,
                                                 Long addOnId,
                                                 UsageRequest request) {

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

        LocalDate startDate = subscription.getStartDate();

        SubscriptionAddOns addOn = subscriptionAddOnRepository
                .findBySubscription_IdAndAddOns_IdAndBillingCycleStart(
                        subscriptionId,
                        addOnId,
                        startDate
                )
                .orElseThrow(() -> new ResourceNotFoundException("Add-on not attached"));

        addOn.setUnitsUsed(addOn.getUnitsUsed() + request.getUnits());

        SubscriptionAddOns saved = subscriptionAddOnRepository.save(addOn);

        // Convert to DTO
        SubscriptionAddOnResponse response = new SubscriptionAddOnResponse();
        response.setId(saved.getId());
        response.setSubscriptionId(saved.getSubscription().getId());
        response.setAddOnId(saved.getAddOns().getId());
        response.setAddOnName(saved.getAddOns().getName());
        response.setUnitsIncluded(saved.getUnitsIncluded());
        response.setUnitsUsed(saved.getUnitsUsed());
        response.setBillingCycleStart(saved.getBillingCycleStart());
        response.setBillingCycleEnd(saved.getBillingCycleEnd());

        return response;
    }

    @Override
    public List<SubscriptionAddOns> getSubscriptionAddOns(Long subscriptionId) {

        return subscriptionAddOnRepository
                .findAll()
                .stream()
                .filter(a -> a.getSubscription().getId().equals(subscriptionId))
                .toList();
    }

    @Override
    public BigDecimal calculateCurrentCycleCharges(Long subscriptionId) {
        List<SubscriptionAddOns> addOns = getSubscriptionAddOns(subscriptionId);

        BigDecimal total = BigDecimal.ZERO;

        for (SubscriptionAddOns addOn:addOns){

            int extraUnits = addOn.getUnitsUsed() - addOn.getUnitsIncluded();

            if(extraUnits > 0){
                BigDecimal extra = addOn.getAddOns().getUnitPrice().multiply(BigDecimal.valueOf(extraUnits));
                total = total.add(extra);
            }

        }
        return total;
    }
}
