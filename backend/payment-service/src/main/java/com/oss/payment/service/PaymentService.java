package com.oss.payment.service;

import com.oss.payment.client.OrderClient;
import com.oss.payment.dto.OrderDTO;
import com.oss.payment.dto.PaymentRequest;
import com.oss.payment.entity.Payment;
import com.oss.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;

    @Transactional
    public Payment processPayment(PaymentRequest request) {
        System.out.println("Processing payment for order ID: " + request.getOrderId());
        OrderDTO order = orderClient.getOrderById(request.getOrderId());

        if ("PAID".equals(order.getStatus())) {
            throw new RuntimeException("Order is already paid");
        }

        if (request.getAmount() == null || order.getTotalAmount() == null) {
            throw new RuntimeException("Payment amount or Order total is missing");
        }

        BigDecimal reqAmount = request.getAmount().setScale(2, RoundingMode.HALF_UP);
        BigDecimal ordAmount = order.getTotalAmount().setScale(2, RoundingMode.HALF_UP);

        if (reqAmount.compareTo(ordAmount) < 0) {
            throw new RuntimeException(
                    "Insufficient payment amount. Required: " + ordAmount + ", Received: " + reqAmount);
        }

        boolean paymentSuccess = true; // Simulate success

        if (paymentSuccess) {
            Payment payment = new Payment();
            payment.setOrderId(order.getId());
            payment.setPaymentMethod(request.getPaymentMethod());
            payment.setAmount(request.getAmount());
            payment.setTransactionId(UUID.randomUUID().toString());
            payment.setStatus("SUCCESS");

            Payment savedPayment = paymentRepository.save(payment);

            orderClient.updateOrderStatus(order.getId(), "PAID");

            System.out.println("Payment successful for order ID: " + order.getId() +
                    ". Transaction ID: " + savedPayment.getTransactionId());

            return savedPayment;
        } else {
            throw new RuntimeException("Payment failed");
        }
    }
}
