package com.example.subscription.controller;

import com.example.subscription.DTO.PaymentRequest;
import com.example.subscription.DTO.RefundRequest;
import com.example.subscription.entity.Payment;
import com.example.subscription.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)

public class PaymentControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private PaymentService paymentService;

        @Autowired
        private ObjectMapper objectMapper;

    @Test
    void shouldProcessPayment() throws Exception {

        Payment payment = new Payment();
        payment.setId(1L);

        when(paymentService.processPayment(
                anyLong(), any(), any(), any()
        )).thenReturn(payment);

        PaymentRequest request = new PaymentRequest();
        request.setSubscriptionId(10L);
        request.setAmount(BigDecimal.valueOf(999));

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldRefundPayment() throws Exception {

        Payment payment = new Payment();
        payment.setId(1L);

        when(paymentService.refundPaymentById(eq(1L), anyString()))
                .thenReturn(payment);

        RefundRequest request = new RefundRequest();
        request.setRefundReason("Customer request");

        mockMvc.perform(put("/api/payments/1/refund")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldReturnPaymentsBySubscription() throws Exception {

        Payment payment = new Payment();
        payment.setId(1L);

        when(paymentService.getPaymentsBySubscription(10L))
                .thenReturn(List.of(payment));

        mockMvc.perform(get("/api/payments/subscription/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }
}
