package com.marketplace.cart.command;

import com.marketplace.cart.entity.Cart;
import com.marketplace.cart.service.CartService;
import com.marketplace.common.command.Command;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class GetCartCommand implements Command<Cart> {

    private final CartService cartService;
    private final UUID userId;

    @Override
    public Cart execute() {
        return cartService.getCart(userId);
    }
}
