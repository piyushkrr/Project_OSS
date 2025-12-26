package com.oss.order.service;

import com.oss.order.entity.*;
import com.oss.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final EmailService emailService;
    private final com.oss.order.client.ProductClient productClient;
    private final com.oss.order.client.UserAuthClient userAuthClient;

    @Transactional
    public Order placeOrder(Long userId, String shippingAddress, String phoneNumber) {
        Cart cart = cartService.getCart(userId);
        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = Order.builder()
                .userId(userId)
                .orderTrackingId(generateTrackingId())
                .status(OrderStatus.PENDING)
                .shippingAddress(shippingAddress)
                .phoneNumber(phoneNumber)
                .totalAmount(cart.getTotalAmount())
                .build();

        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> OrderItem.builder()
                        .order(order)
                        .productId(cartItem.getProductId())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getPrice())
                        .build())
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(userId); // Clear cart after order

        try {
            com.oss.order.dto.UserDTO user = userAuthClient.getUserById(userId);
            savedOrder.setUser(user);
        } catch (Exception e) {
            // Log error but continue
            System.err.println("Failed to fetch user details for email: " + e.getMessage());
        }

        emailService.sendOrderConfirmation(savedOrder);

        return savedOrder;
    }

    public List<Order> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        orders.forEach(this::populateProductDetails);
        return orders;
    }

    public Order getOrder(Long orderId) {
        Order order = orderRepository.findWithItemsById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        populateProductDetails(order);
        return order;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAllByOrderByCreatedAtDesc();
        orders.forEach(this::populateProductDetails);
        orders.forEach(this::populateUserDetails);
        return orders;
    }

    private void populateUserDetails(Order order) {
        if (order != null && order.getUserId() != null) {
            try {
                com.oss.order.dto.UserDTO user = userAuthClient.getUserById(order.getUserId());
                order.setUser(user);
            } catch (Exception e) {
                // Ignore or log error
            }
        }
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = getOrder(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    private void populateProductDetails(Order order) {
        if (order != null && order.getOrderItems() != null) {
            order.getOrderItems().forEach(item -> {
                try {
                    item.setProduct(productClient.getProductById(item.getProductId()));
                } catch (Exception e) {
                    // Ignore or log error
                }
            });
        }
    }

    private String generateTrackingId() {
        return "ORD-" + System.currentTimeMillis() + "-" + String.format("%04d", (int) (Math.random() * 10000));
    }
}
