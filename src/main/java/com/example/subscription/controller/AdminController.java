package com.example.subscription.controller;

import com.example.subscription.scheduler.DunningScheduler;
import com.example.subscription.scheduler.RenewalScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {


    private final RenewalScheduler renewalScheduler;
    private final DunningScheduler dunningScheduler;

    @PostMapping("/trigger-renewal")
    public void triggerRenewal(){
        renewalScheduler.runRenewals();
    }

    @PostMapping("/trigger-dunning")
    public void triggerDunning(){
        dunningScheduler.runDunning();
    }

}
