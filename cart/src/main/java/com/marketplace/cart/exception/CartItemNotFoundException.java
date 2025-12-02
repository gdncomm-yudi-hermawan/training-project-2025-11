package com.marketplace.cart.exception;

import com.marketplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a cart item is not found
 */
public class CartItemNotFoundException extends BaseException {

    public CartItemNotFoundException(String productId) {
        super("Cart item not found for product ID: " + productId,
                HttpStatus.NOT_FOUND.value(),
                "CART_ITEM_NOT_FOUND");
    }
}
