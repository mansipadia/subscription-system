package com.example.subscription.controller;

import com.example.subscription.scheduler.RenewalScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RenewalDebugController {

    @Autowired
    private RenewalScheduler renewalScheduler;

    @PostMapping("/run")
    public String runNow(){
        renewalScheduler.runRenewals();
        return "Renewal job executed";
    }
}
