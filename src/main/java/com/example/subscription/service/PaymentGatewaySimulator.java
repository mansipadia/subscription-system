package com.example.subscription.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class PaymentGatewaySimulator {
    public boolean process(BigDecimal amount){
        return amount.remainder(BigDecimal.valueOf(2)).compareTo(BigDecimal.ZERO) == 0;
//       return false;
    }
}
