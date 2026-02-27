package com.example.subscription.unit;

import com.example.subscription.DTO.CreatePlanRequest;
import com.example.subscription.DTO.PlanResponse;
import com.example.subscription.entity.Plan;
import com.example.subscription.enums.PlanType;
import com.example.subscription.exception.ResourceNotFoundException;
import com.example.subscription.repository.PlanRepository;
import com.example.subscription.service.impl.PlanServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlanServiceTest {

    @Mock
    private PlanRepository planRepository;

    @InjectMocks
    private PlanServiceImpl planService;

    @Test
    void savePlanData_shouldSavePlanSuccessfully() {
        CreatePlanRequest request = new CreatePlanRequest();
        request.setTier(PlanType.BASIC);
        request.setPrice(BigDecimal.valueOf(999));
        request.setName("Basic Plan");
        request.setDescription("Basic Plan desc");
        request.setDurationDays(30);

        Plan savedPlan = new Plan();
        savedPlan.setId(1L);
        savedPlan.setName("Premium");


        when((planRepository.save(any(Plan.class)))).thenReturn(savedPlan);

        PlanResponse response = planService.savePlanData(request);

        assertNotNull(response);
        assertEquals("Premium", response.getName());
        verify(planRepository, times(1)).save(any(Plan.class));
    }

    @Test
    void getAllPlan_shouldReturnAllPlans() {
        Plan plan = new Plan();
        plan.setId(1L);
        plan.setName("Basic");

        when(planRepository.findAll()).thenReturn(List.of(plan));

        List<PlanResponse> response = planService.getAllPlan();

        assertEquals(1, response.size());
        assertEquals("Basic", response.get(0).getName());
    }

    @Test
    void getPlansById_shouldReturnPlan() {
        Plan plan = new Plan();
        plan.setId(1L);

        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));

        PlanResponse response = planService.getPlansById(1L);

        assertEquals(1L, response.getId());
    }

    @Test
    void getPlansById_shouldThrowExceptionIfNotFound() {
        when(planRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> planService.getPlansById(1L));
    }
}
