package com.example.subscription.controller;

import com.example.subscription.DTO.CreatePlanRequest;
import com.example.subscription.DTO.CreateUserRequest;
import com.example.subscription.DTO.PlanResponse;
import com.example.subscription.DTO.UserResponse;
import com.example.subscription.enums.PlanType;
import com.example.subscription.service.PlanService;
import com.example.subscription.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlanControllerTest {

    private MockMvc mockMvc;
    private PlanService planService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        planService = Mockito.mock(PlanService.class);

        PlanController planController = new PlanController(planService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(planController)
                .build();
    }

    @Test
    void createPlans_success() throws Exception {

        CreatePlanRequest request= new CreatePlanRequest();
        request.setName("testing");
        request.setDescription("Plan description");
        request.setPrice(BigDecimal.valueOf(2000));
        request.setTier(PlanType.BASIC);
        request.setDurationDays(20);

        PlanResponse response = new PlanResponse();
        response.setId(1L);
        response.setName("testing");
        response.setDescription("Plan description");
        response.setPrice(BigDecimal.valueOf(2000));
        response.setTier(PlanType.BASIC);
        response.setDurationDays(20);

        Mockito.when(planService.savePlanData(any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("testing"));
    }

}
