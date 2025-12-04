package com.marketplace.cart.service;

import com.marketplace.cart.dto.AddToCartRequest;
import com.marketplace.cart.entity.Cart;
import com.marketplace.cart.entity.CartItem;
import com.marketplace.cart.exception.CartNotFoundException;
import com.marketplace.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    @Transactional
    public Cart addToCart(UUID userId, AddToCartRequest request) {
        log.info("Adding item to cart for user: {}, product: {}", userId, request.getProductId());

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    log.info("Creating new cart for user: {}", userId);
                    return Cart.builder().userId(userId).build();
                });

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            log.info("Updated quantity for product {} to {}", request.getProductId(), item.getQuantity());
        } else {
            CartItem newItem = CartItem.builder()
                    .productId(request.getProductId())
                    .productName(request.getProductName())
                    .price(request.getPrice())
                    .quantity(request.getQuantity())
                    .build();
            cart.addItem(newItem);
            log.info("Added new item to cart: {}", request.getProductId());
        }

        Cart savedCart = cartRepository.save(cart);
        log.info("Cart saved successfully for user: {}", userId);
        return savedCart;
    }

    public Cart getCart(UUID userId) {
        log.info("Fetching cart for user: {}", userId);
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    log.info("No cart found for user: {}, returning empty cart", userId);
                    return Cart.builder().userId(userId).build();
                });
    }

    @Transactional
    public Cart removeFromCart(UUID userId, String productId) {
        log.info("Removing product {} from cart for user: {}", productId, userId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));

        int initialSize = cart.getItems().size();
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));

        if (cart.getItems().size() == initialSize) {
            log.warn("Product {} not found in cart for user: {}", productId, userId);
        } else {
            log.info("Removed product {} from cart for user: {}", productId, userId);
        }

        Cart savedCart = cartRepository.save(cart);
        log.info("Cart updated successfully for user: {}", userId);
        return savedCart;
    }
}
