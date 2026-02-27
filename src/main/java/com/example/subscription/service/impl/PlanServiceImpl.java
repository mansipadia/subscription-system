package com.example.subscription.service.impl;

import com.example.subscription.DTO.CreatePlanRequest;
import com.example.subscription.DTO.PlanResponse;
import com.example.subscription.DTO.UpdatePlanRequest;
import com.example.subscription.entity.Plan;
import com.example.subscription.exception.ResourceNotFoundException;
import com.example.subscription.repository.PlanRepository;
import com.example.subscription.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {

    @Autowired
    PlanRepository planRepository;

    @Override
    public PlanResponse savePlanData(CreatePlanRequest request) {
        Plan plan = new Plan();
        plan.setName(request.getName());
        plan.setPrice(request.getPrice());
        plan.setTier(request.getTier());
        plan.setDescription(request.getDescription());
        plan.setDuration_days(request.getDurationDays());

        Plan saved = planRepository.save(plan);

        return mapToResponse(saved);
    }

    private PlanResponse mapToResponse(Plan plan) {
        PlanResponse response = new PlanResponse();
        response.setId(plan.getId());
        response.setName(plan.getName());
        response.setDescription(plan.getDescription());
        response.setPrice(plan.getPrice());
        response.setActive(plan.getActive());
        response.setTier(plan.getTier());
        response.setDurationDays(plan.getDuration_days());
        return response;

    }

    @Override
    public List<PlanResponse> getAllPlan() {
        return planRepository.findAll().stream().map(this::mapToResponse).toList();
    }


    @Override
    public PlanResponse getPlansById(Long id) {
        Plan plan = planRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Plan not found : "+id));
        return mapToResponse(plan);
    }

    @Override
    public PlanResponse updatePlanById(Long id, UpdatePlanRequest request) {

        Plan existingPlan = planRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Plan is not found : "+id));

            if (request.getName() != null)
                existingPlan.setName(request.getName());

            if (request.getPrice() != null)
                existingPlan.setPrice(request.getPrice());

            if (request.getDurationDays() != null)
                existingPlan.setDuration_days(request.getDurationDays());

            if (request.getDescription() != null)
                existingPlan.setDescription(request.getDescription());

            if (request.getTier() != null)
                existingPlan.setTier(request.getTier());

            if (request.getActive() != null)
                existingPlan.setActive(request.getActive());

            Plan updated = planRepository.save(existingPlan);
            return mapToResponse(updated);
    }

}
