package com.example.subscription.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "dunning")
public class DunningConfig {

    private int maxRetries;
    private int gracePeriodDays;
    private List<Integer> retryIntervalsHours;
    private Long schedulerIntervalMS;


}
