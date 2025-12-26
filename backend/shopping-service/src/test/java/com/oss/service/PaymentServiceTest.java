package com.oss.service;

import com.oss.dto.PaymentRequest;
import com.oss.entity.Order;
import com.oss.entity.OrderStatus;
import com.oss.entity.Payment;
import com.oss.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private PaymentService paymentService;

    private Order order;
    private PaymentRequest paymentRequest;

    @BeforeEach
    public void setup() {
        order = Order.builder()
                .id(1L)
                .totalAmount(new BigDecimal("100.00"))
                .status(OrderStatus.PENDING)
                .build();

        paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId(1L);
        paymentRequest.setAmount(new BigDecimal("100.00"));
        paymentRequest.setPaymentMethod("Credit Card");
    }

    @Test
    public void testProcessPayment_Success() {
        when(orderService.getOrder(1L)).thenReturn(order);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.processPayment(paymentRequest);

        assertNotNull(payment);
        assertEquals("SUCCESS", payment.getStatus());
        verify(orderService, times(1)).updateOrderStatus(1L, OrderStatus.PAID);
    }

    @Test
    public void testProcessPayment_InsufficientAmount() {
        paymentRequest.setAmount(new BigDecimal("50.00"));
        when(orderService.getOrder(1L)).thenReturn(order);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            paymentService.processPayment(paymentRequest);
        });

        assertEquals("Insufficient payment amount", exception.getMessage());
        verify(paymentRepository, never()).save(any(Payment.class));
    }
}
