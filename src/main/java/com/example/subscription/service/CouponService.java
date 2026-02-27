package com.example.subscription.service;

import com.example.subscription.DTO.CouponRequest;
import com.example.subscription.DTO.CouponResponse;

import java.util.List;
public interface CouponService {

    CouponResponse saveCouponData(CouponRequest request);
    List<CouponResponse> getAllCoupons();
    CouponResponse  getCouponsById(Long id);


}
