package com.example.subscription.service;

import com.example.subscription.DTO.CreatePlanRequest;
import com.example.subscription.DTO.PlanResponse;
import com.example.subscription.DTO.UpdatePlanRequest;

import java.util.List;

public interface PlanService {

    PlanResponse savePlanData(CreatePlanRequest request);
    List<PlanResponse> getAllPlan();
    PlanResponse getPlansById(Long id);
    PlanResponse updatePlanById(Long id, UpdatePlanRequest request);


}
