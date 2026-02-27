package com.example.subscription.controller;


import com.example.subscription.DTO.CreatePlanRequest;
import com.example.subscription.DTO.PlanResponse;
import com.example.subscription.DTO.UpdatePlanRequest;
import com.example.subscription.service.PlanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PlanController {


    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }


    @PostMapping("/plans")
    public ResponseEntity<?> addPlan(@Valid @RequestBody CreatePlanRequest request){
        PlanResponse response = planService.savePlanData(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/plans")
    public ResponseEntity<List<PlanResponse>> getAllPlans(){
        return ResponseEntity.ok(planService.getAllPlan());
    }

    @GetMapping("/plans/{id}")
    public ResponseEntity<PlanResponse> getPlanById(@PathVariable("id") Long id){
        return ResponseEntity.ok(planService.getPlansById(id));
    }

    @PutMapping("/plans/{id}/change-plan")
    public ResponseEntity<PlanResponse> updatePlan(@PathVariable Long id,@Valid @RequestBody UpdatePlanRequest request){

        return ResponseEntity.ok(planService.updatePlanById(id,request));

        }
    }
