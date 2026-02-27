package com.example.subscription.repository;

import com.example.subscription.entity.Coupon;
import com.example.subscription.entity.CouponUsage;
import com.example.subscription.entity.User;
import com.example.subscription.enums.CouponType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CouponUsageRepositoryTest {

    @Autowired
    private CouponUsageRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldCountCouponUsageByUser() {

        User user = createUser();
        Coupon coupon = createCoupon();

        CouponUsage usage = new CouponUsage();
        usage.setUser(user);
        usage.setCoupon(coupon);
        usage.setUsageCount(3);
        entityManager.persist(usage);

        entityManager.flush();

        long count = repository.countByCouponAndUser(coupon, user);

        assertThat(count).isEqualTo(1);
    }

    @Test
    void shouldReturnZeroWhenNoUsageExists() {

        User user = createUser();
        Coupon coupon = createCoupon();

        long count = repository.countByCouponAndUser(coupon, user);

        assertThat(count).isZero();
    }

    @Test
    void shouldFindCouponUsageByUserAndCoupon() {

        User user = createUser();
        Coupon coupon = createCoupon();

        CouponUsage usage = new CouponUsage();
        usage.setUser(user);
        usage.setCoupon(coupon);
        usage.setUsageCount(2);
        entityManager.persist(usage);

        entityManager.flush();

        var result = repository.findByUserAndCoupon(user, coupon);

        assertThat(result).isPresent();
        assertThat(result.get().getUsageCount()).isEqualTo(2);
        assertThat(result.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void shouldReturnEmptyWhenCouponUsageNotFound() {

        User user = createUser();
        Coupon coupon = createCoupon();

        var result = repository.findByUserAndCoupon(user, coupon);

        assertThat(result).isEmpty();
    }


    private User createUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setPassword("123456");
        return entityManager.persist(user);
    }

    private Coupon createCoupon() {
        Coupon coupon = new Coupon();
        coupon.setCode("SAVE20");
        coupon.setDiscountPercentage(BigDecimal.valueOf(10));
        coupon.setActive(true);
        coupon.setType(CouponType.PERCENTAGE);
        coupon.setExpiryDate(LocalDate.now().plusDays(5));
        coupon.setUsageLimit(10);
        return entityManager.persist(coupon);
    }
}