package com.example.subscription.unit;

import com.example.subscription.DTO.CouponRequest;
import com.example.subscription.DTO.CouponResponse;
import com.example.subscription.entity.Coupon;
import com.example.subscription.enums.CouponType;
import com.example.subscription.repository.CouponRepository;
import com.example.subscription.service.impl.CouponServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @InjectMocks
    private CouponServiceImpl couponService;

    @Mock
    private CouponRepository couponRepository;

    @Test
    void shouldSavePercentageCoupon(){
        CouponRequest request = new CouponRequest();
        request.setCode("DISC10");
        request.setType(CouponType.PERCENTAGE);
        request.setDiscountPercentage(BigDecimal.valueOf(10));
        request.setActive(true);
        request.setExpiryDate(LocalDate.now().plusDays(5));
        request.setUsageLimit(5);
        request.setUsedCount(0);

        Coupon saved = new Coupon();
        saved.setId(1L);
        saved.setType(CouponType.PERCENTAGE);
        saved.setDiscountPercentage(BigDecimal.valueOf(10));
        saved.setActive(true);
        saved.setExpiryDate(LocalDate.now().plusDays(5));
        saved.setUsageLimit(5);
        saved.setUsedCount(0);

        when(couponRepository.save(any(Coupon.class))).thenReturn(saved);

        CouponResponse response = couponService.saveCouponData(request);

        assertNotNull(response);
        assertEquals(1L,response.getId());
        verify(couponRepository).save(any(Coupon.class));
    }


}
