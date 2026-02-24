package com.example.subscription.service.impl;

import com.example.subscription.DTO.CouponRequest;
import com.example.subscription.DTO.CouponResponse;
import com.example.subscription.entity.Coupon;
import com.example.subscription.exception.ResourceNotFoundException;
import com.example.subscription.repository.CouponRepository;
import com.example.subscription.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.example.subscription.enums.CouponType.PERCENTAGE;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    CouponRepository couponRepository;

    @Override
    public CouponResponse saveCouponData(CouponRequest request){

        Coupon coupon = new Coupon();

        coupon.setCode(request.getCode());
        coupon.setType(request.getType());
        coupon.setActive(request.getActive());
        coupon.setType(request.getType());
        coupon.setDiscountAmount(request.getDiscountAmount());
        coupon.setDiscountPercentage(request.getDiscountPercentage());
        coupon.setExpiryDate(request.getExpiryDate());
        coupon.setUsageLimit(request.getUsageLimit());
        coupon.setUsedCount(request.getUsedCount());

        switch (request.getType()) {

            case PERCENTAGE:
                if (request.getDiscountPercentage() == null)
                    throw new IllegalArgumentException("discountPercentage required for PERCENTAGE type");
                break;

            case AMOUNT:
                if (request.getDiscountAmount() == null)
                    throw new IllegalArgumentException("discountAmount required for AMOUNT type");
                break;

            case BOTH:
                if (request.getDiscountPercentage() == null ||
                        request.getDiscountAmount() == null)
                    throw new IllegalArgumentException("Both discountPercentage and discountAmount required");
                break;

            case FREE_TRIAL:
                break;
        }

        Coupon saved = couponRepository.save(coupon);
        return mapToResponse(saved);
    }

    @Override
    public List<CouponResponse> getAllCoupons() {
        return couponRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    private CouponResponse mapToResponse(Coupon coupon) {
        CouponResponse response = new CouponResponse();
        response.setId(coupon.getId());
        response.setCode(coupon.getCode());
        response.setActive(coupon.getActive());
        response.setDiscountAmount(coupon.getDiscountAmount());
        response.setDiscountPercentage(coupon.getDiscountPercentage());
        response.setType(coupon.getType());
        response.setUsageLimit(coupon.getUsageLimit());
        response.setUsedCount(coupon.getUsedCount());
        response.setExpiryDate(coupon.getExpiryDate());

        return response;
    }

    @Override
    public CouponResponse getCouponsById(Long id) {
        Coupon coupon = couponRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Coupon is not found : "+id));
        return mapToResponse(coupon);
    }

    public CouponResponse validateCoupon(String code) {

        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Invalid coupon"));

        if (!coupon.getActive())
            throw new RuntimeException("Coupon inactive");

        if (coupon.getExpiryDate().isBefore(LocalDate.now()))
            throw new RuntimeException("Coupon expired");

        if (coupon.getUsedCount() >= coupon.getUsageLimit())
            throw new RuntimeException("Coupon usage limit reached");

        return mapToResponse(coupon);
    }

}
