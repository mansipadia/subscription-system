package com.example.subscription.controller;

import com.example.subscription.DTO.AddOnRequest;
import com.example.subscription.entity.AddOns;
import com.example.subscription.service.AddOnService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/add-ons")
@RequiredArgsConstructor
public class AddOnController {

    @Autowired
    AddOnService addOnService;

    @PostMapping
    public AddOns create(@RequestBody AddOnRequest request){
        return addOnService.createAddOn(request);
    }

    @GetMapping
    public List<AddOns> list(){
        return addOnService.getActiveAddOns();
    }
}
