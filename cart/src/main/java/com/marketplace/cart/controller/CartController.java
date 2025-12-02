package com.marketplace.cart.controller;

import com.marketplace.cart.dto.AddToCartRequest;
import com.marketplace.cart.dto.response.CartResponse;
import com.marketplace.cart.mapper.CartMapper;
import com.marketplace.cart.service.CartService;
import com.marketplace.common.dto.ApiResponse;
import com.marketplace.common.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @Valid @RequestBody AddToCartRequest request) {

        String username = extractUsername(token);
        log.info("Add to cart request from user: {}", username);

        CartResponse cartResponse = CartMapper.toCartResponse(
                cartService.addToCart(username, request));

        return ResponseEntity.ok(ApiResponse.success("Item added to cart successfully", cartResponse));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        String username = extractUsername(token);
        log.info("Get cart request from user: {}", username);

        CartResponse cartResponse = CartMapper.toCartResponse(
                cartService.getCart(username));

        return ResponseEntity.ok(ApiResponse.success(cartResponse));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<CartResponse>> removeFromCart(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable String productId) {

        String username = extractUsername(token);
        log.info("Remove from cart request from user: {}, product: {}", username, productId);

        CartResponse cartResponse = CartMapper.toCartResponse(
                cartService.removeFromCart(username, productId));

        return ResponseEntity.ok(ApiResponse.success("Item removed from cart successfully", cartResponse));
    }

    private String extractUsername(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return jwtUtil.extractUsername(token.substring(7));
        }
        throw new RuntimeException("Invalid token");
    }
}
