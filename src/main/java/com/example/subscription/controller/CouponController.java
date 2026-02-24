package com.example.subscription.controller;

import com.example.subscription.DTO.CouponRequest;
import com.example.subscription.DTO.CouponResponse;
import com.example.subscription.entity.Coupon;
import com.example.subscription.service.CouponService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/coupons")
    public ResponseEntity<CouponResponse> addCoupon(@Valid @RequestBody CouponRequest request) {
        CouponResponse response = couponService.saveCouponData(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<CouponResponse>> getAllCoupon() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    @GetMapping("/coupons/{id}")
    public ResponseEntity<CouponResponse> getCouponById(@PathVariable Long id) {
        return ResponseEntity.ok(couponService.getCouponsById(id));
    }
}
