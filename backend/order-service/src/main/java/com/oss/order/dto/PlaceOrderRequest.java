package com.oss.order.dto;

import lombok.Data;

@Data
public class PlaceOrderRequest {
    private String shippingAddress;
    private String phoneNumber;
}
