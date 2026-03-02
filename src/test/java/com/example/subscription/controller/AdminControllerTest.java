package com.example.subscription.controller;

import com.example.subscription.scheduler.DunningScheduler;
import com.example.subscription.scheduler.RenewalScheduler;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    RenewalScheduler renewalScheduler;

    @MockitoBean
    DunningScheduler dunningScheduler;


    @Test
    void shouldTriggerRenewal() throws Exception {

        mockMvc.perform(post("/api/admin/trigger-renewal"))
                .andExpect(status().isAccepted());

        verify(renewalScheduler).runRenewals();
    }

    @Test
    void shouldTriggerDunning() throws Exception {

        mockMvc.perform(post("/api/admin/trigger-dunning"))
                .andExpect(status().isAccepted());

        verify(dunningScheduler).runDunning();
    }
}
