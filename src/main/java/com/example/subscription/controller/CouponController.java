package com.example.subscription.controller;

import com.example.subscription.entity.Coupon;
import com.example.subscription.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Coupon> addCoupon(@RequestBody Coupon coupon) {
        return ResponseEntity.ok(couponService.saveCouponData(coupon));
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<Coupon>> getAllCoupon() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    @GetMapping("coupons/{id}")
    public ResponseEntity<Coupon> getCouponById(@PathVariable Long id) {
        return couponService.getCouponsById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
