package com.oss.service;

import com.oss.entity.*;
import com.oss.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Cart cart;

    @BeforeEach
    public void setup() {
        user = User.builder().id(1L).email("test@example.com").build();

        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(new BigDecimal("100.00"))
                .build();

        CartItem cartItem = CartItem.builder()
                .product(product)
                .quantity(1)
                .price(product.getPrice()) // 100
                .build();

        List<CartItem> items = new ArrayList<>();
        items.add(cartItem);

        cart = Cart.builder()
                .user(user)
                .cartItems(items)
                .build();
    }

    @Test
    public void testPlaceOrder_Success() {
        when(cartService.getCart(user)).thenReturn(cart);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(123L);
            return order;
        });

        Order createdOrder = orderService.placeOrder(user, "123 Street", "555-0199");

        assertNotNull(createdOrder);
        assertEquals(123L, createdOrder.getId());
        assertEquals(1, createdOrder.getOrderItems().size());
        assertEquals(new BigDecimal("100.00"), createdOrder.getTotalAmount());

        verify(cartService, times(1)).clearCart(user);
    }

    @Test
    public void testPlaceOrder_EmptyCart() {
        cart.getCartItems().clear();
        when(cartService.getCart(user)).thenReturn(cart);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderService.placeOrder(user, "Address", "123");
        });

        assertEquals("Cart is empty", exception.getMessage());
    }
}
