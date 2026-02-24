package com.example.subscription.service;

import com.example.subscription.DTO.CouponRequest;
import com.example.subscription.DTO.CouponResponse;
import com.example.subscription.entity.Coupon;
import com.example.subscription.entity.Plan;

import java.util.List;
public interface CouponService {

    CouponResponse saveCouponData(CouponRequest request);
    List<CouponResponse> getAllCoupons();
    CouponResponse  getCouponsById(Long id);


}
