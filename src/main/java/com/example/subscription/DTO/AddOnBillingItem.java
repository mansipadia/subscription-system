package com.example.subscription.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddOnBillingItem {

    private String addonName;
    private int totalUsage;
    private int unitsIncluded;
    private int billableUnits;
    private BigDecimal pricePerUnit;
    private BigDecimal amount;

    public AddOnBillingItem(String addonName,
                            int totalUsage,
                            int unitsIncluded,
                            int billableUnits,
                            BigDecimal pricePerUnit,
                            BigDecimal amount) {
        this.addonName = addonName;
        this.totalUsage = totalUsage;
        this.unitsIncluded = unitsIncluded;
        this.billableUnits = billableUnits;
        this.pricePerUnit = pricePerUnit;

        this.amount = amount;
    }

}