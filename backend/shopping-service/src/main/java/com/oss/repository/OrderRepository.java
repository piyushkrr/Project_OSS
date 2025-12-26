package com.oss.repository;

import com.oss.entity.Order;
import com.oss.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.user = :user ORDER BY o.createdAt DESC")
    List<Order> findByUserOrderByCreatedAtDesc(@org.springframework.data.repository.query.Param("user") User user);

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderItems ORDER BY o.createdAt DESC")
    List<Order> findAllByOrderByCreatedAtDesc();

    @org.springframework.data.jpa.repository.Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems JOIN FETCH o.user WHERE o.id = :id")
    java.util.Optional<Order> findWithItemsById(@org.springframework.data.repository.query.Param("id") Long id);
}
