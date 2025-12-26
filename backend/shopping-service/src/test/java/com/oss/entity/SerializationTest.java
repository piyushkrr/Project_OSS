package com.oss.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SerializationTest {

    @Test
    public void testOrderSerialization() {
        // Mock User
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");

        // Mock Product
        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(BigDecimal.TEN)
                .build();

        // Mock Order
        Order order = Order.builder()
                .id(1L)
                .user(user)
                .totalAmount(BigDecimal.TEN)
                .status(OrderStatus.PAID)
                .createdAt(LocalDateTime.now())
                .orderItems(new ArrayList<>())
                .build();

        // Mock OrderItem
        OrderItem item = OrderItem.builder()
                .id(1L)
                .order(order)
                .product(product)
                .quantity(1)
                .price(BigDecimal.TEN)
                .build();

        order.getOrderItems().add(item);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // Attempt Serialization
        assertDoesNotThrow(() -> {
            String json = mapper.writeValueAsString(order);
            System.out.println("Serialized JSON: " + json);
        });
    }
}
