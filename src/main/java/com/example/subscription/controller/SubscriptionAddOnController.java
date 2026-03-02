package com.example.subscription.controller;

import com.example.subscription.DTO.AddOnBillingResponse;
import com.example.subscription.DTO.AttachAddOnRequest;
import com.example.subscription.DTO.SubscriptionAddOnResponse;
import com.example.subscription.DTO.UsageRequest;
import com.example.subscription.entity.SubscriptionAddOns;
import com.example.subscription.service.SubscriptionAddOnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionAddOnController {

    @Autowired
    SubscriptionAddOnService subscriptionAddOnService;


    @PostMapping("/{id}/add-ons")
    public SubscriptionAddOnResponse attachAddOn(@PathVariable Long id, @RequestBody AttachAddOnRequest request){
        return subscriptionAddOnService.attachAddOn(id,request);
    }

    @PostMapping("/{id}/add-ons/{addOnId}/usage")
    public SubscriptionAddOnResponse recordUsage(@PathVariable Long id, @PathVariable Long addOnId, @RequestBody UsageRequest request){
        return subscriptionAddOnService.recordUsage(id,addOnId,request);
    }

    @GetMapping("/{id}/add-ons")
    public List<SubscriptionAddOns> getSubscriptionAddOns(@PathVariable Long id){
        return subscriptionAddOnService.getSubscriptionAddOns(id);
    }

    @GetMapping("/{id}/add-ons/billing")
    public ResponseEntity<AddOnBillingResponse> calculateAddOnBilling(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionAddOnService.calculateCurrentCycleCharges(id));
    }
}
