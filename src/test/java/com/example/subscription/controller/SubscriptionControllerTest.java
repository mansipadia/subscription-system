package com.example.subscription.controller;

import com.example.subscription.DTO.ChangePlanRequest;
import com.example.subscription.DTO.SubscriptionRequest;
import com.example.subscription.DTO.SubscriptionResponse;
import com.example.subscription.enums.PaymentMethod;
import com.example.subscription.enums.PaymentType;
import com.example.subscription.enums.PlanType;
import com.example.subscription.service.SubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubscriptionController.class)
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SubscriptionService subscriptionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateSubscription() throws Exception {

        SubscriptionRequest request = new SubscriptionRequest();
        request.setUserId(1L);
        request.setPlanId(2L);
        request.setCouponCode("DISC10");
        request.setMethod(PaymentMethod.CARD);
        request.setType(PaymentType.SUBSCRIPTION);

        SubscriptionResponse response = new SubscriptionResponse();
        response.setId(100L);
        response.setUser_id(1L);

        Mockito.when(subscriptionService.createSubscription(
                        anyLong(), anyLong(), anyString(), any(), any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100L));
    }

    @Test
    void shouldReturnAllSubscriptions() throws Exception {

        SubscriptionResponse response = new SubscriptionResponse();
        response.setId(1L);

        Mockito.when(subscriptionService.getAllSubscriptions())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void shouldReturnSubscriptionById() throws Exception {

        SubscriptionResponse response = new SubscriptionResponse();
        response.setId(5L);

        Mockito.when(subscriptionService.getSubscriptionById(5L))
                .thenReturn(response);

        mockMvc.perform(get("/api/subscriptions/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L));
    }

    @Test
    void shouldCancelSubscription() throws Exception {

        SubscriptionResponse response = new SubscriptionResponse();
        response.setId(7L);

        Mockito.when(subscriptionService.cancelSubscription(7L))
                .thenReturn(response);

        mockMvc.perform(put("/api/subscriptions/7/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7L));
    }

    @Test
    void shouldChangePlan() throws Exception {

        ChangePlanRequest request = new ChangePlanRequest();
        request.setNewPlanId(3L);
        request.setMethod(PaymentMethod.CARD);

        SubscriptionResponse response = new SubscriptionResponse();
        response.setId(9L);

        Mockito.when(subscriptionService.changePlan(
                        eq(9L), eq(3L), eq(PaymentMethod.CARD)))
                .thenReturn(response);

        mockMvc.perform(put("/api/subscriptions/9/change-plan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(9L));
    }
}