package com.example.subscription.service.impl;

import com.example.subscription.DTO.AddOnRequest;
import com.example.subscription.entity.AddOns;
import com.example.subscription.exception.ResourceNotFoundException;
import com.example.subscription.repository.AddOnRepository;
import com.example.subscription.service.AddOnService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddOnServiceImpl implements AddOnService {

    @Autowired
    AddOnRepository addOnRepository;

    @Override
    public AddOns createAddOn(AddOnRequest request) {

        AddOns addOns = new AddOns();

        addOns.setName(request.getName());
        addOns.setDescription(request.getDescription());
        addOns.setUnitName(request.getUnitName());
        addOns.setUnitPrice(request.getUnitPrice());

        return addOnRepository.save(addOns);

    }


    @Override
    @Transactional(readOnly = true)
    public List<AddOns> getActiveAddOns() {
        return addOnRepository.findByActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public AddOns getById(Long id) {
        return addOnRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Add-on not found"));
    }
}
