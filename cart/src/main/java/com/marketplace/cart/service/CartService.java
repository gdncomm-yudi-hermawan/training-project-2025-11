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

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    @Transactional
    public Cart addToCart(String username, AddToCartRequest request) {
        log.info("Adding item to cart for user: {}, product: {}", username, request.getProductId());

        Cart cart = cartRepository.findByUsername(username)
                .orElseGet(() -> {
                    log.info("Creating new cart for user: {}", username);
                    return Cart.builder().username(username).build();
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
        log.info("Cart saved successfully for user: {}", username);
        return savedCart;
    }

    public Cart getCart(String username) {
        log.info("Fetching cart for user: {}", username);
        return cartRepository.findByUsername(username)
                .orElseGet(() -> {
                    log.info("No cart found for user: {}, returning empty cart", username);
                    return Cart.builder().username(username).build();
                });
    }

    @Transactional
    public Cart removeFromCart(String username, String productId) {
        log.info("Removing product {} from cart for user: {}", productId, username);

        Cart cart = cartRepository.findByUsername(username)
                .orElseThrow(() -> new CartNotFoundException(username));

        int initialSize = cart.getItems().size();
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));

        if (cart.getItems().size() == initialSize) {
            log.warn("Product {} not found in cart for user: {}", productId, username);
        } else {
            log.info("Removed product {} from cart for user: {}", productId, username);
        }

        Cart savedCart = cartRepository.save(cart);
        log.info("Cart updated successfully for user: {}", username);
        return savedCart;
    }
}
