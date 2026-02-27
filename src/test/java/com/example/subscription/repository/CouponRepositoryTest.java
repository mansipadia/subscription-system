package com.example.subscription.repository;

import com.example.subscription.entity.Coupon;
import com.example.subscription.enums.CouponType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CouponRepositoryTest {

    @Autowired
    private CouponRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldFindCouponByCode() {

        Coupon coupon = new Coupon();
        coupon.setCode("SAVE10");
        coupon.setDiscountPercentage(BigDecimal.valueOf(10));
        coupon.setExpiryDate(LocalDate.now().plusDays(10));
        coupon.setActive(true);
        coupon.setUsageLimit(10);
        coupon.setType(CouponType.PERCENTAGE );

        entityManager.persist(coupon);
        entityManager.flush();

        Optional<Coupon> result = repository.findByCode("SAVE10");

        assertThat(result).isPresent();
        assertThat(result.get().getCode()).isEqualTo("SAVE10");
    }

    @Test
    void shouldReturnEmptyWhenCouponNotFound() {

        Optional<Coupon> result = repository.findByCode("INVALID");

        assertThat(result).isEmpty();
    }
}