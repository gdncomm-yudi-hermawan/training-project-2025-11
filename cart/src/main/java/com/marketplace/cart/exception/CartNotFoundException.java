package com.marketplace.cart.exception;

import com.marketplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a cart is not found for a user
 */
public class CartNotFoundException extends BaseException {

    public CartNotFoundException(String username) {
        super("Cart not found for user: " + username,
                HttpStatus.NOT_FOUND.value(),
                "CART_NOT_FOUND");
    }
}
