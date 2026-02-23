package com.example.subscription.service;

import com.example.subscription.entity.Coupon;
import com.example.subscription.entity.Plan;

import java.util.List;
import java.util.Optional;

public interface CouponService {

    Coupon saveCouponData(Coupon coupon);
    List<Coupon> getAllCoupons();
    Optional<Coupon> getCouponsById(Long id);


}
