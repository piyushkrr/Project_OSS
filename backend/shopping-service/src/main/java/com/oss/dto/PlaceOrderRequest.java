package com.oss.dto;

import lombok.Data;

@Data
public class PlaceOrderRequest {
    private String shippingAddress;
    private String phoneNumber;
}
