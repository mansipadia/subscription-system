package com.example.subscription.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@RequiredArgsConstructor
public class AddOnBillingResponse {
    public BigDecimal totalAddOnCharge;
    public BigDecimal grandTotal;
    public List<AddOnBillingItem> billingItem;
}
