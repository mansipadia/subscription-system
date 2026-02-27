package com.example.subscription.controller;

import com.example.subscription.DTO.ChangePlanRequest;
import com.example.subscription.DTO.SubscriptionRequest;
import com.example.subscription.DTO.SubscriptionResponse;
import com.example.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionResponse> createSubscription(@Valid @RequestBody SubscriptionRequest request){
        SubscriptionResponse response = subscriptionService.createSubscription(request.getUserId(), request.getPlanId(), request.getCouponCode(),request.getMethod(),request.getType());

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> getSubscription(){
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponse> getSubscriptionById(@PathVariable Long id){
        return ResponseEntity.ok(subscriptionService.getSubscriptionById(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<SubscriptionResponse> cancelSubscription(@PathVariable Long id){
        return ResponseEntity.ok(subscriptionService.cancelSubscription(id));
    }

    @PutMapping("/{id}/change-plan")
    public ResponseEntity<SubscriptionResponse> changePlan(@PathVariable Long id, @RequestBody ChangePlanRequest changePlanRequest){
        return ResponseEntity.ok(subscriptionService.changePlan(id,changePlanRequest.getNewPlanId(), changePlanRequest.getMethod()));
    }

}
