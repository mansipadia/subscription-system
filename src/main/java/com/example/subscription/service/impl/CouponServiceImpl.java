package com.example.subscription.service.impl;

import com.example.subscription.entity.Coupon;
import com.example.subscription.repository.CouponRepository;
import com.example.subscription.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    CouponRepository couponRepository;

    @Override
    public Coupon saveCouponData(Coupon coupon){

        switch (coupon.getType()) {

            case PERCENTAGE:
                if (coupon.getDiscountPercentage() == null)
                    throw new IllegalArgumentException("discountPercentage required for PERCENTAGE type");
                break;

            case AMOUNT:
                if (coupon.getDiscountAmount() == null)
                    throw new IllegalArgumentException("discountAmount required for AMOUNT type");
                break;

            case BOTH:
                if (coupon.getDiscountPercentage() == null ||
                        coupon.getDiscountAmount() == null)
                    throw new IllegalArgumentException("Both discountPercentage and discountAmount required");
                break;

            case FREE_TRIAL:
                break;
        }

        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    public Optional<Coupon> getCouponsById(Long id) {
        return couponRepository.findById(id);
    }

    public Coupon validateCoupon(String code) {

        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Invalid coupon"));

        if (!coupon.getActive())
            throw new RuntimeException("Coupon inactive");

        if (coupon.getExpiryDate().isBefore(LocalDate.now()))
            throw new RuntimeException("Coupon expired");

        if (coupon.getUsedCount() >= coupon.getUsageLimit())
            throw new RuntimeException("Coupon usage limit reached");

        return coupon;
    }

}
