package com.example.subscription.DTO;

import lombok.Data;

@Data
public class AttachAddOnRequest {

    public Long subscriptionId;
    public Long addOnId;
    public int unitsIncluded;
}
