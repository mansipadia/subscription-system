package com.example.subscription.service;

import com.example.subscription.entity.Plan;

import java.util.List;
import java.util.Optional;

public interface PlanService {

    Plan savePlanData(Plan plan);
    List<Plan> getAllPlan();
    Optional<Plan> getPlansById(Long id);
    Optional<Plan> updatePlanById(Long id, Plan updatedPlan);


}
