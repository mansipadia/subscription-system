package com.example.subscription.service.impl;

import com.example.subscription.DTO.SubscriptionResponse;
import com.example.subscription.entity.*;
import com.example.subscription.enums.*;
import com.example.subscription.exception.ConflictException;
import com.example.subscription.exception.ResourceNotFoundException;
import com.example.subscription.repository.*;
import com.example.subscription.service.PaymentService;
import com.example.subscription.service.SubscriptionService;
import com.example.subscription.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    PaymentService paymentService;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    CouponUsageRepository couponUsageRepository;

    @Autowired
    PaymentRepository paymentRepository;


    @Override
    public SubscriptionResponse createSubscription(Long userId, Long planId, String couponCode, PaymentMethod method, PaymentType type) {

        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found : "+userId));

        Plan plan = planRepository.findById(planId).orElseThrow(()->new ResourceNotFoundException("Plan not found : "+planId));

        BigDecimal finalPrice = plan.getPrice();

        Coupon coupon = null;

        if (couponCode != null){
            coupon = validateCoupon(couponCode,user);
            finalPrice = applyDiscount(finalPrice,coupon);
        }


        Subscription subscription = new Subscription();

        subscription.setUser(user);
        subscription.setPlan(plan);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusDays(plan.getDuration_days()));
        subscription.setStatus(SubscriptionStatus.PENDING);
        subscription.setFinalPrice(finalPrice);

        subscription = subscriptionRepository.saveAndFlush(subscription);

        paymentService.processPayment(subscription.getId(), finalPrice, method,type);

        if (coupon != null) {
            incrementCouponUsage(coupon, user);
        }

        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscriptionRepository.save(subscription);
        return mapToResponse(subscription);
    }

    @Transactional
    private void incrementCouponUsage(Coupon coupon, User user) {

        coupon.setUsedCount(coupon.getUsedCount() + 1);
        couponRepository.save(coupon);

        CouponUsage usage = couponUsageRepository.findByUserAndCoupon(user, coupon).orElse(null);

        if (usage == null){
            usage = new CouponUsage();
            usage.setCoupon(coupon);
            usage.setUser(user);
            usage.setUsageCount(1);
        }
        else {
            usage.setUsageCount(usage.getUsageCount() +1);
        }


        couponUsageRepository.save(usage);
    }

    private BigDecimal applyDiscount(BigDecimal price, Coupon coupon) {

        BigDecimal finalPrice =price;

        switch (coupon.getType()){
            case PERCENTAGE:
                finalPrice= price.subtract(price.multiply(coupon.getDiscountPercentage().divide(BigDecimal.valueOf(100))));
                break;
            case AMOUNT:
                finalPrice = price.subtract(coupon.getDiscountAmount());
                break;
            case BOTH:
                BigDecimal percentageDis = price.multiply(coupon.getDiscountPercentage().divide(BigDecimal.valueOf(100)));

                BigDecimal afterPercentage =price.subtract(percentageDis);

                finalPrice = afterPercentage.subtract(coupon.getDiscountAmount());
                break;
            case FREE_TRIAL:
                finalPrice = BigDecimal.ZERO;
                break;
        }

        if (finalPrice.compareTo(BigDecimal.ZERO) < 0){
            finalPrice = BigDecimal.ZERO;
        }
        return finalPrice.setScale(2,RoundingMode.HALF_UP);
    }

    private Coupon validateCoupon(String code, User user) {

        Coupon coupon = couponRepository.findByCode(code).orElseThrow(()->new ResourceNotFoundException("Coupon not found"));

        if(!coupon.getActive()){
            throw new BadRequestException("Coupon Inactive");
        }

        if (coupon.getExpiryDate().isBefore(LocalDate.now())){
            throw  new BadRequestException("Coupon Expired..");
        }

        if (coupon.getUsedCount() >= coupon.getUsageLimit()) {
            throw new ConflictException("Coupon usage limit reached");
        }

        CouponUsage usage = couponUsageRepository
                .findByUserAndCoupon(user, coupon)
                .orElse(null);

        if (usage != null && usage.getUsageCount() >= 1) {
            throw new ConflictException("User already used this coupon");
        }
        return coupon;

    }

    private SubscriptionResponse mapToResponse(Subscription subscription) {
        SubscriptionResponse response = new SubscriptionResponse();
        response.setId(subscription.getId());
        response.setUser_id(subscription.getUser().getId());
        response.setPlan_id(subscription.getPlan().getId());
        response.setStatus(subscription.getStatus());
        response.setEndDate(subscription.getEndDate());
        response.setFinalPrice(subscription.getFinalPrice());
        response.setStartDate(subscription.getStartDate());
        response.setCreatedAt(subscription.getCreatedAt());
        response.setUpdatedAt(subscription.getUpdatedAt());
        return response;
    }

    @Transactional
    @Override
    public SubscriptionResponse cancelSubscription(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        subscription.setStatus(SubscriptionStatus.CANCELLED);
        return mapToResponse(subscription);
    }

    @Override
    @Transactional
    public SubscriptionResponse changePlan(Long subscriptionId,
                                           Long newPlanId,
                                           PaymentMethod method) {

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

        if (subscription.getStatus() == SubscriptionStatus.CANCELLED) {
            throw new BadRequestException("Subscription is cancelled. Cannot change plan.");
        }

        Plan newPlan = planRepository.findById(newPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("New plan not found"));

        long remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), subscription.getEndDate());
        remainingDays = Math.max(0, remainingDays);

        long totalDays = subscription.getPlan().getDuration_days();

        remainingDays = Math.min(remainingDays, totalDays);

        BigDecimal credit = subscription.getPlan().getPrice()
                .multiply(BigDecimal.valueOf(remainingDays))
                .divide(BigDecimal.valueOf(totalDays), 2, RoundingMode.HALF_UP);

        BigDecimal upgradeAmount = newPlan.getPrice().subtract(credit);

        upgradeAmount = upgradeAmount.setScale(0, RoundingMode.HALF_UP);

        if (upgradeAmount.remainder(BigDecimal.valueOf(2))
                .compareTo(BigDecimal.ZERO) != 0) {

            upgradeAmount = upgradeAmount.add(BigDecimal.ONE);
        }


        if (upgradeAmount.compareTo(BigDecimal.ZERO) > 0) {

            paymentService.processPayment(
                    subscription.getId(),
                    upgradeAmount,
                    method,
                    PaymentType.UPGRADE
            );

        } else if (upgradeAmount.compareTo(BigDecimal.ZERO) < 0) {

            BigDecimal refundAmount = upgradeAmount.abs();

            Payment lastPayment = paymentRepository
                    .findTopBySubscriptionIdAndPaymentStatusOrderByPaymentDateDesc(
                            subscription.getId(),
                            PaymentStatus.SUCCESS
                    )
                    .orElseThrow(() -> new BadRequestException("No successful payment found"));

            paymentService.refundPayment(lastPayment, refundAmount);
        }

        subscription.setPlan(newPlan);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusDays(newPlan.getDuration_days()));
        subscription.setStatus(SubscriptionStatus.ACTIVE);


        subscription.setFinalPrice(newPlan.getPrice());

        subscriptionRepository.save(subscription);
        return mapToResponse(subscription);
    }
    @Override
    public List<SubscriptionResponse> getAllSubscriptions() {
        return subscriptionRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public SubscriptionResponse getSubscriptionById(Long id){
        Subscription subscription = subscriptionRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Subscription not found : "+id));
        return mapToResponse(subscription);
    }
}
