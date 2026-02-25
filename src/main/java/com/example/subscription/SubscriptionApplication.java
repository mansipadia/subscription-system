package com.example.subscription;

import com.example.subscription.config.DunningConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(DunningConfig.class)
public class SubscriptionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubscriptionApplication.class, args);
	}

}
