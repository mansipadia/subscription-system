package com.example.subscription.service;

import com.example.subscription.DTO.AddOnRequest;
import com.example.subscription.entity.AddOns;

import java.util.List;

public interface AddOnService {

    AddOns createAddOn(AddOnRequest request);
    List<AddOns>  getActiveAddOns();
    AddOns getById(Long id);
}
