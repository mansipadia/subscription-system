package com.example.subscription.controller;

import com.example.subscription.DTO.AddOnRequest;
import com.example.subscription.entity.AddOns;
import com.example.subscription.service.AddOnService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddOnController.class)
public class AddOnControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    AddOnService addOnService;

    @Test
    void shouldCreateAddOn() throws Exception{
        AddOns addOns = new AddOns();
        addOns.setId(1L);
        AddOnRequest request = new AddOnRequest();
        request.setName("add on name");
        request.setDescription("add on description");
        request.setUnitName("unit name");
        request.setUnitPrice(BigDecimal.valueOf(200));

        when(addOnService.createAddOn(any(AddOnRequest.class))).thenReturn(addOns);

        mockMvc.perform(post("/api/add-ons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        //verify(addOnService).createAddOn(any(AddOnRequest.class));
    }

    @Test
    void shouldReturnListOfAddOns() throws Exception {

        AddOns addOn = new AddOns();
        addOn.setName("Extra Session");
        addOn.setUnitName("Session");
        addOn.setUnitPrice(BigDecimal.valueOf(100));

        when(addOnService.getActiveAddOns())
                .thenReturn(List.of(addOn));

        mockMvc.perform(get("/api/add-ons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Extra Session"))
                .andExpect(jsonPath("$[0].unitName").value("Session"))
                .andExpect(jsonPath("$[0].unitPrice").value(100));

        verify(addOnService).getActiveAddOns();
    }
}
