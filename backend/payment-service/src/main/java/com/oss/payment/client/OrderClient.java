package com.oss.payment.client;

import com.oss.payment.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service")
public interface OrderClient {
    @GetMapping("/api/orders/{id}")
    OrderDTO getOrderById(@PathVariable("id") Long id);

    @PutMapping("/api/orders/admin/{orderId}/status")
    void updateOrderStatus(@PathVariable("orderId") Long orderId, @RequestParam("status") String status);
}
