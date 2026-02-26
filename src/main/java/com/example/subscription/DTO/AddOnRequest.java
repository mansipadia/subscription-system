package com.example.subscription.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddOnRequest {

    public String name;
    public String description;
    public String unitName;
    public BigDecimal unitPrice;
}
