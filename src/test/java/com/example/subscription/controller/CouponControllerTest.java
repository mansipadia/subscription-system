package com.example.subscription.controller;

import com.example.subscription.DTO.CouponRequest;
import com.example.subscription.DTO.CouponResponse;
import com.example.subscription.enums.CouponType;
import com.example.subscription.service.CouponService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CouponController.class)
class CouponControllerTest {

        @Autowired
        MockMvc mockMvc;

        @MockitoBean
        CouponService couponService;

        @Autowired
        ObjectMapper objectMapper;

    @Test
    void shouldCreateCoupon() throws Exception {

        CouponResponse response = new CouponResponse();
        response.setId(1L);
        response.setCode("NEWYEAR");
        response.setDiscountPercentage(BigDecimal.valueOf(20));
        response.setUsageLimit(10);
        response.setExpiryDate(LocalDate.now().plusDays(2));


        when(couponService.saveCouponData(any()))
                .thenReturn(response);

        CouponRequest request = new CouponRequest();
        request.setCode("NEWYEAR");
        request.setDiscountPercentage(BigDecimal.valueOf(20));
        request.setUsageLimit(10);
        request.setExpiryDate(LocalDate.now().plusDays(2));
        request.setType(CouponType.PERCENTAGE);


        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.code").value("NEWYEAR"))
                .andExpect(jsonPath("$.discountPercentage").value(20));
    }

    @Test
    void shouldReturnAllCoupons() throws Exception {

        CouponResponse c1 = new CouponResponse();
        c1.setId(1L);
        c1.setCode("NEWYEAR");

        CouponResponse c2 = new CouponResponse();
        c2.setId(2L);
        c2.setCode("SUMMER");

        when(couponService.getAllCoupons())
                .thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/api/coupons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("NEWYEAR"))
                .andExpect(jsonPath("$[1].code").value("SUMMER"));
    }

    @Test
    void shouldReturnCouponById() throws Exception {

        CouponResponse response = new CouponResponse();
        response.setId(1L);
        response.setCode("NEWYEAR");

        when(couponService.getCouponsById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/coupons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.code").value("NEWYEAR"));
    }

    @Test
    void shouldReturn400WhenValidationFails() throws Exception {

        CouponRequest request = new CouponRequest();
        // No fields set -> assume required fields exist

        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


}
