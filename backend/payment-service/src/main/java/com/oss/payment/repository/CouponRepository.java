package com.oss.payment.repository;

import com.oss.payment.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCodeAndIsActiveTrue(String code);

    List<Coupon> findAllByIsActiveTrue();

    boolean existsByCode(String code);
}
