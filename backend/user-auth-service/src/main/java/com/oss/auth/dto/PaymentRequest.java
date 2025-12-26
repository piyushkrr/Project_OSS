package com.oss.auth.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private Long orderId;
    private String paymentMethod;
    private BigDecimal amount;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private String upiId;
}
