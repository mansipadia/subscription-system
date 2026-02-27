package com.example.subscription.unit;

import com.example.subscription.DTO.AttachAddOnRequest;
import com.example.subscription.DTO.SubscriptionAddOnResponse;
import com.example.subscription.DTO.UsageRequest;
import com.example.subscription.entity.*;
import com.example.subscription.exception.ResourceNotFoundException;
import com.example.subscription.repository.*;
import com.example.subscription.service.impl.SubscriptionAddOnServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionAddOnServiceTest {


        @Mock
        SubscriptionRepository subscriptionRepository;

        @Mock
        SubscriptionAddOnRepository subscriptionAddOnRepository;

        @Mock
        AddOnRepository addOnRepository;

        @InjectMocks
        SubscriptionAddOnServiceImpl service;

    @Test
    void shouldAttachAddOnSuccessfully() {

        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setStartDate(LocalDate.of(2025,1,1));
        subscription.setEndDate(LocalDate.of(2025,1,31));

        AddOns addOns = new AddOns();
        addOns.setId(10L);
        addOns.setName("Extra Storage");

        AttachAddOnRequest request = new AttachAddOnRequest();
        request.setAddOnId(10L);
        request.setUnitsIncluded(100);

        when(subscriptionRepository.findById(1L))
                .thenReturn(Optional.of(subscription));

        when(addOnRepository.findById(10L))
                .thenReturn(Optional.of(addOns));

        when(subscriptionAddOnRepository.save(any()))
                .thenAnswer(invocation -> {
                    SubscriptionAddOns s = invocation.getArgument(0);
                    s.setId(99L);
                    return s;
                });

        SubscriptionAddOnResponse response =
                service.attachAddOn(1L, request);

        assertEquals(99L, response.getId());
        assertEquals(1L, response.getSubscriptionId());
        assertEquals(10L, response.getAddOnId());
        assertEquals(100, response.getUnitsIncluded());
    }

    @Test
    void shouldThrowIfSubscriptionNotFound() {

        AttachAddOnRequest request = new AttachAddOnRequest();
        request.setAddOnId(10L);

        when(subscriptionRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                service.attachAddOn(1L, request)
        );
    }

    @Test
    void shouldRecordUsageSuccessfully() {

        SubscriptionAddOns addOn = new SubscriptionAddOns();
        addOn.setUnitsUsed(10);
        addOn.setUnitsIncluded(50);

        UsageRequest request = new UsageRequest();
        request.setUnits(5);

        when(subscriptionAddOnRepository
                .findBySubscription_IdAndAddOns_IdAndBillingCycleStart(
                        anyLong(), anyLong(), any()))
                .thenReturn(Optional.of(addOn));

        when(subscriptionAddOnRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SubscriptionAddOnResponse updated =
                service.recordUsage(1L, 10L, request);

        assertEquals(15, updated.getUnitsUsed());
    }

    @Test
    void shouldThrowIfAddOnNotAttached() {

        UsageRequest request = new UsageRequest();
        request.setUnits(5);

        when(subscriptionAddOnRepository
                .findBySubscription_IdAndAddOns_IdAndBillingCycleStart(
                        anyLong(), anyLong(), any()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                service.recordUsage(1L, 10L, request)
        );
    }

    @Test
    void shouldReturnOnlyMatchingSubscriptionAddOns() {

        Subscription sub1 = new Subscription();
        sub1.setId(1L);

        Subscription sub2 = new Subscription();
        sub2.setId(2L);

        SubscriptionAddOns a1 = new SubscriptionAddOns();
        a1.setSubscription(sub1);

        SubscriptionAddOns a2 = new SubscriptionAddOns();
        a2.setSubscription(sub2);

        when(subscriptionAddOnRepository.findAll())
                .thenReturn(List.of(a1, a2));

        List<SubscriptionAddOns> result =
                service.getSubscriptionAddOns(1L);

        assertEquals(1, result.size());
    }
}
