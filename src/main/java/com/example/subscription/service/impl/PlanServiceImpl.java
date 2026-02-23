package com.example.subscription.service.impl;

import com.example.subscription.entity.Plan;
import com.example.subscription.repository.PlanRepository;
import com.example.subscription.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanServiceImpl implements PlanService {

    @Autowired
    PlanRepository planRepository;

    @Override
    public Plan savePlanData(Plan plan) {
        return planRepository.save(plan);
    }

    @Override
    public List<Plan> getAllPlan() {
        return planRepository.findAll();
    }


    @Override
    public Optional<Plan> getPlansById(Long id) {
        return planRepository.findById(id);
    }

    @Override
    public Optional<Plan> updatePlanById(Long id, Plan updatedPlan) {

        return planRepository.findById(id).map(existingPlan -> {

            if (updatedPlan.getName() != null)
                existingPlan.setName(updatedPlan.getName());

            if (updatedPlan.getPrice() != null)
                existingPlan.setPrice(updatedPlan.getPrice());

            if (updatedPlan.getDuration_days() != null)
                existingPlan.setDuration_days(updatedPlan.getDuration_days());

            if (updatedPlan.getDescription() != null)
                existingPlan.setDescription(updatedPlan.getDescription());

            if (updatedPlan.getTier() != null)
                existingPlan.setTier(updatedPlan.getTier());

            if (updatedPlan.getActive() != null)
                existingPlan.setActive(updatedPlan.getActive());

            return planRepository.save(existingPlan);
        });
    }
}