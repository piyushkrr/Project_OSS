package com.oss.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "saved_payments")
public class SavedPaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private String type; // CARD, UPI, NET_BANKING

    @Column(nullable = false)
    private String provider; // Visa, Mastercard, GooglePay, HDFC

    @Column(nullable = false)
    private String maskedNumber; // **** 1234 or user@upi

    private String cardHolderName;
    private String expiryDate;

    // In a real app, you'd store a token from the payment gateway here.
    // private String paymentToken;
}
