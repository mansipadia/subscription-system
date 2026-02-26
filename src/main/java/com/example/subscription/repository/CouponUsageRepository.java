package com.example.subscription.repository;

import com.example.subscription.entity.Coupon;
import com.example.subscription.entity.CouponUsage;
import com.example.subscription.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponUsageRepository extends JpaRepository<CouponUsage, Long> {

    long countByCouponAndUser(Coupon coupon, User user);

    Optional<CouponUsage> findByUserAndCoupon(User user, Coupon coupon);
}
