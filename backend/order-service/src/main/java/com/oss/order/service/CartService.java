package com.oss.order.service;

import com.oss.order.client.ProductClient;
import com.oss.order.dto.ProductDTO;
import com.oss.order.entity.Cart;
import com.oss.order.entity.CartItem;
import com.oss.order.repository.CartItemRepository;
import com.oss.order.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductClient productClient;

    public Cart getCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder().userId(userId).build();
                    return cartRepository.save(newCart);
                });
        populateProductDetails(cart);
        return cart;
    }

    @Transactional
    public Cart addToCart(Long userId, Long productId, int quantity) {
        Cart cart = getCart(userId);
        ProductDTO product = productClient.getProductById(productId);

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .productId(productId)
                    .quantity(quantity)
                    .price(product.getPrice())
                    .build();
            cart.getCartItems().add(newItem);
            cartItemRepository.save(newItem);
        }

        Cart savedCart = cartRepository.save(cart);
        populateProductDetails(savedCart);
        return savedCart;
    }

    private void populateProductDetails(Cart cart) {
        if (cart != null && cart.getCartItems() != null) {
            cart.getCartItems().forEach(item -> {
                try {
                    item.setProduct(productClient.getProductById(item.getProductId()));
                } catch (Exception e) {
                    // Ignore or log error
                }
            });
        }
    }

    @Transactional
    public Cart removeFromCart(Long userId, Long productId) {
        Cart cart = getCart(userId);
        cart.getCartItems().removeIf(item -> item.getProductId().equals(productId));
        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getCart(userId);
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }
}
