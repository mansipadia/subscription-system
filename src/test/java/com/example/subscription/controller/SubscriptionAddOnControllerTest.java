package com.example.subscription.controller;

import com.example.subscription.DTO.AttachAddOnRequest;
import com.example.subscription.DTO.SubscriptionAddOnResponse;
import com.example.subscription.DTO.UsageRequest;
import com.example.subscription.entity.Subscription;
import com.example.subscription.entity.SubscriptionAddOns;
import com.example.subscription.service.SubscriptionAddOnService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubscriptionAddOnController.class)
public class SubscriptionAddOnControllerTest {

        @Autowired
        MockMvc mockMvc;

        @MockitoBean
        SubscriptionAddOnService subscriptionAddOnService;

        @Autowired
        ObjectMapper objectMapper;

    @Test
    void shouldAttachAddOn() throws Exception {

        SubscriptionAddOnResponse response = new SubscriptionAddOnResponse();
        response.setId(100L);
        response.setSubscriptionId(1L);
        response.setAddOnId(10L);
        response.setAddOnName("Extra Storage");
        response.setUnitsIncluded(100);

        when(subscriptionAddOnService.attachAddOn(eq(1L), any()))
                .thenReturn(response);

        AttachAddOnRequest request = new AttachAddOnRequest();
        request.setAddOnId(10L);
        request.setUnitsIncluded(100);

        mockMvc.perform(post("/api/subscription/1/add-ons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.subscriptionId").value(1L))
                .andExpect(jsonPath("$.addOnId").value(10L))
                .andExpect(jsonPath("$.unitsIncluded").value(100));
    }

    @Test
    void shouldRecordUsage() throws Exception {

        Subscription subscription = new Subscription();
        subscription.setId(1L);

        SubscriptionAddOns addOn = new SubscriptionAddOns();
        addOn.setId(200L);
        addOn.setUnitsUsed(50);
        addOn.setSubscription(subscription);

        SubscriptionAddOnResponse response = new SubscriptionAddOnResponse();
        response.setId(200L);
        response.setUnitsUsed(50);

        when(subscriptionAddOnService.recordUsage(eq(1L), eq(10L), any()))
                .thenReturn(response);

        UsageRequest request = new UsageRequest();
        request.setUnits(10);

        mockMvc.perform(post("/api/subscription/1/add-ons/10/usage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(200L))
                .andExpect(jsonPath("$.unitsUsed").value(50));
    }

    @Test
    void shouldReturnSubscriptionAddOns() throws Exception {

        Subscription subscription = new Subscription();
        subscription.setId(1L);

        SubscriptionAddOns addOn = new SubscriptionAddOns();
        addOn.setId(300L);
        addOn.setUnitsUsed(20);
        addOn.setSubscription(subscription);

        when(subscriptionAddOnService.getSubscriptionAddOns(1L))
                .thenReturn(List.of(addOn));

        mockMvc.perform(get("/api/subscription/1/add-ons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(300L))
                .andExpect(jsonPath("$[0].unitsUsed").value(20));
    }

    @Test
    void shouldReturn404IfAddOnNotFound() throws Exception {

        when(subscriptionAddOnService.attachAddOn(eq(1L), any()))
                .thenThrow(new RuntimeException("Not Found"));

        AttachAddOnRequest request = new AttachAddOnRequest();
        request.setAddOnId(10L);

        mockMvc.perform(post("/api/subscription/1/add-ons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }
}
