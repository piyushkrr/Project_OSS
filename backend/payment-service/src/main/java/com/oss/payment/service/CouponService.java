package com.oss.payment.service;

import com.oss.payment.dto.CouponValidationResponse;
import com.oss.payment.entity.Coupon;
import com.oss.payment.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public List<Coupon> getActiveCoupons() {
        return couponRepository.findAllByIsActiveTrue();
    }

    public Coupon getCouponById(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
    }

    @Transactional
    public Coupon createCoupon(Coupon coupon) {
        // Check if code already exists
        if (couponRepository.existsByCode(coupon.getCode())) {
            throw new RuntimeException("Coupon code already exists");
        }

        // Set defaults
        if (coupon.getIsActive() == null) {
            coupon.setIsActive(true);
        }
        if (coupon.getUsedCount() == null) {
            coupon.setUsedCount(0);
        }

        return couponRepository.save(coupon);
    }

    @Transactional
    public Coupon updateCoupon(Long id, Coupon couponData) {
        Coupon existingCoupon = getCouponById(id);

        // Update fields
        if (couponData.getDescription() != null) {
            existingCoupon.setDescription(couponData.getDescription());
        }
        if (couponData.getMinOrderValue() != null) {
            existingCoupon.setMinOrderValue(couponData.getMinOrderValue());
        }
        if (couponData.getDiscountAmount() != null) {
            existingCoupon.setDiscountAmount(couponData.getDiscountAmount());
        }
        if (couponData.getIsActive() != null) {
            existingCoupon.setIsActive(couponData.getIsActive());
        }
        if (couponData.getExpiryDate() != null) {
            existingCoupon.setExpiryDate(couponData.getExpiryDate());
        }
        if (couponData.getUsageLimit() != null) {
            existingCoupon.setUsageLimit(couponData.getUsageLimit());
        }

        return couponRepository.save(existingCoupon);
    }

    @Transactional
    public void deleteCoupon(Long id) {
        if (!couponRepository.existsById(id)) {
            throw new RuntimeException("Coupon not found");
        }
        couponRepository.deleteById(id);
    }

    public CouponValidationResponse validateCoupon(String code, BigDecimal orderTotal) {
        // Find active coupon
        Coupon coupon = couponRepository.findByCodeAndIsActiveTrue(code)
                .orElse(null);

        if (coupon == null) {
            return CouponValidationResponse.builder()
                    .valid(false)
                    .message("Invalid coupon code")
                    .build();
        }

        // Check if expired
        if (coupon.getExpiryDate() != null && LocalDateTime.now().isAfter(coupon.getExpiryDate())) {
            return CouponValidationResponse.builder()
                    .valid(false)
                    .message("Coupon has expired")
                    .build();
        }

        // Check usage limit
        if (coupon.getUsageLimit() != null && coupon.getUsedCount() >= coupon.getUsageLimit()) {
            return CouponValidationResponse.builder()
                    .valid(false)
                    .message("Coupon usage limit reached")
                    .build();
        }

        // Check minimum order value
        if (coupon.getMinOrderValue() != null && orderTotal.compareTo(coupon.getMinOrderValue()) < 0) {
            return CouponValidationResponse.builder()
                    .valid(false)
                    .message(String.format("Coupon requires minimum order value of ₹%s", coupon.getMinOrderValue()))
                    .build();
        }

        // Valid coupon
        return CouponValidationResponse.builder()
                .valid(true)
                .code(coupon.getCode())
                .discountAmount(coupon.getDiscountAmount())
                .minOrderValue(coupon.getMinOrderValue())
                .description(coupon.getDescription())
                .message(String.format("Coupon applied! You saved ₹%s", coupon.getDiscountAmount()))
                .build();
    }

    @Transactional
    public void incrementUsageCount(String code) {
        couponRepository.findByCodeAndIsActiveTrue(code).ifPresent(coupon -> {
            coupon.setUsedCount(coupon.getUsedCount() + 1);
            couponRepository.save(coupon);
        });
    }
}
