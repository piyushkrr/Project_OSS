package com.oss.payment.config;

import com.oss.payment.entity.Coupon;
import com.oss.payment.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class CouponDataSeeder {

    @Bean
    public CommandLineRunner seedCoupons(CouponRepository couponRepository) {
        return args -> {
            if (couponRepository.count() == 0) {
                // Create initial coupons
                Coupon offer1000 = Coupon.builder()
                        .code("OFFER1000")
                        .description("₹200 OFF on orders above ₹{1,000")
                        .minOrderValue(new BigDecimal("1000"))
                        .discountAmount(new BigDecimal("200"))
                        .isActive(true)
                        .expiryDate(LocalDateTime.now().plusMonths(6))
                        .usageLimit(1000)
                        .usedCount(0)
                        .build();

                Coupon offer2000 = Coupon.builder()
                        .code("OFFER2000")
                        .description("₹500 OFF on orders above ₹2,000")
                        .minOrderValue(new BigDecimal("2000"))
                        .discountAmount(new BigDecimal("500"))
                        .isActive(true)
                        .expiryDate(LocalDateTime.now().plusMonths(6))
                        .usageLimit(1000)
                        .usedCount(0)
                        .build();

                Coupon mega5000 = Coupon.builder()
                        .code("MEGA5000")
                        .description("₹1,200 OFF on orders above ₹5,000")
                        .minOrderValue(new BigDecimal("5000"))
                        .discountAmount(new BigDecimal("1200"))
                        .isActive(true)
                        .expiryDate(LocalDateTime.now().plusMonths(6))
                        .usageLimit(500)
                        .usedCount(0)
                        .build();

                couponRepository.save(offer1000);
                couponRepository.save(offer2000);
                couponRepository.save(mega5000);

                System.out.println("✅ Coupon data seeded successfully!");
            }
        };
    }
}
