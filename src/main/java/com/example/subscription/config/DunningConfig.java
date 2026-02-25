package com.example.subscription.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Data
@ConfigurationProperties(prefix = "dunning")
public class DunningConfig {

    private int maxRetries;
    private int gracePeriodDays;
    private List<Integer> retryIntervalHours;
    private Long schedulerIntervalMS;


}
