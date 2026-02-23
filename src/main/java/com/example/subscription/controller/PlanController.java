package com.example.subscription.controller;


import com.example.subscription.entity.Plan;
import com.example.subscription.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PlanController {

    @Autowired
    PlanService planService;

    @PostMapping("/plans")
    public ResponseEntity addPlan(@RequestBody Plan plan){
        try{
            planService.savePlanData(plan);
            return new ResponseEntity(HttpStatusCode.valueOf(200));
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }
    }

    @GetMapping("/plans")
    public ResponseEntity<List<Plan>> getAllPlans(){
        try{
            return new ResponseEntity<>(planService.getAllPlan(),HttpStatusCode.valueOf(200));
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }
    }

    @GetMapping("/plans/{id}")
    public ResponseEntity<Optional<Plan>> getPlanById(@PathVariable("id") Long id){
        try {
            return new ResponseEntity<>(planService.getPlansById(id),HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }
    }

    @PutMapping("/plans/{id}")
    public ResponseEntity<Plan> updatePlan(@PathVariable Long id, @RequestBody Plan updatedPlan){
        try {
            return planService.updatePlanById(id,updatedPlan)
                    .map(plan -> ResponseEntity.ok(plan))
                    .orElse(ResponseEntity.notFound().build());
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }
    }
}
