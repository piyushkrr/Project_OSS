package com.oss.service;

import com.oss.dto.PaymentRequest;
import com.oss.entity.Order;
import com.oss.entity.OrderStatus;
import com.oss.entity.Payment;
import com.oss.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final com.oss.repository.OrderRepository orderRepository;
    private final OrderService orderService;
    private final EmailService emailService;

    @Transactional
    public Payment processPayment(PaymentRequest request) {
        log.info("Processing payment for order ID: {}", request.getOrderId());
        Order order = orderRepository.findWithItemsById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with items"));

        if (order.getStatus() == OrderStatus.PAID) {
            throw new RuntimeException("Order is already paid");
        }

        if (request.getAmount() == null || order.getTotalAmount() == null) {
            throw new RuntimeException("Payment amount or Order total is missing");
        }

        // Robust comparison: round to 2 decimal places to avoid minor precision issues
        java.math.BigDecimal reqAmount = request.getAmount().setScale(2, java.math.RoundingMode.HALF_UP);
        java.math.BigDecimal ordAmount = order.getTotalAmount().setScale(2, java.math.RoundingMode.HALF_UP);

        if (reqAmount.compareTo(ordAmount) < 0) {
            throw new RuntimeException(
                    "Insufficient payment amount. Required: " + ordAmount + ", Received: " + reqAmount);
        }

        // Mock payment processing logic
        // In a real system, this would interact with a payment gateway (e.g., Stripe,
        // PayPal)
        boolean paymentSuccess = true; // Simulate success

        if (paymentSuccess) {
            Payment payment = Payment.builder()
                    .order(order)
                    .paymentMethod(request.getPaymentMethod())
                    .amount(request.getAmount())
                    .transactionId(UUID.randomUUID().toString())
                    .status("SUCCESS")
                    .build();

            Payment savedPayment = paymentRepository.save(payment);

            orderService.updateOrderStatus(order.getId(), OrderStatus.PAID);

            // Send email confirmation
            emailService.sendOrderConfirmation(order.getId());

            return savedPayment;
        } else {
            throw new RuntimeException("Payment failed");
        }
    }
}
