package com.marketplace.cart.command;

import com.marketplace.cart.client.ProductServiceClient;
import com.marketplace.cart.command.impl.AddToCartCommandImpl;
import com.marketplace.cart.command.impl.GetCartCommandImpl;
import com.marketplace.cart.command.impl.RemoveFromCartCommandImpl;
import com.marketplace.cart.dto.AddToCartRequest;
import com.marketplace.cart.dto.request.AddToCartCommandRequest;
import com.marketplace.cart.dto.request.GetCartRequest;
import com.marketplace.cart.dto.request.RemoveFromCartRequest;
import com.marketplace.cart.dto.response.CartResponse;
import com.marketplace.cart.dto.response.ProductDetailsResponse;
import com.marketplace.cart.entity.Cart;
import com.marketplace.cart.entity.CartItem;
import com.marketplace.cart.exception.CartNotFoundException;
import com.marketplace.cart.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartCommandTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductServiceClient productServiceClient;

    private AddToCartCommandImpl addToCartCommand;
    private GetCartCommandImpl getCartCommand;
    private RemoveFromCartCommandImpl removeFromCartCommand;

    @BeforeEach
    void setUp() {
        addToCartCommand = new AddToCartCommandImpl(cartRepository, productServiceClient);
        getCartCommand = new GetCartCommandImpl(cartRepository);
        removeFromCartCommand = new RemoveFromCartCommandImpl(cartRepository);
    }

    @Test
    void addToCart_NewCart_Success() {
        UUID userId = UUID.randomUUID();
        AddToCartRequest addRequest = new AddToCartRequest();
        addRequest.setProductId("p1");
        addRequest.setQuantity(1);

        AddToCartCommandRequest request = AddToCartCommandRequest.builder()
                .userId(userId)
                .addToCartRequest(addRequest)
                .build();

        // Mock product service response
        ProductDetailsResponse productDetails = ProductDetailsResponse.builder()
                .id("p1")
                .name("Product 1")
                .price(BigDecimal.TEN)
                .build();
        when(productServiceClient.getProductById("p1")).thenReturn(productDetails);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CartResponse result = addToCartCommand.execute(request);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(1, result.getItems().size());
        assertEquals("p1", result.getItems().get(0).getProductId());
        assertEquals("Product 1", result.getItems().get(0).getProductName());
        verify(productServiceClient).getProductById("p1");
    }

    @Test
    void addToCart_ExistingCart_NewItem_Success() {
        UUID userId = UUID.randomUUID();
        AddToCartRequest addRequest = new AddToCartRequest();
        addRequest.setProductId("p2");
        addRequest.setQuantity(1);

        AddToCartCommandRequest request = AddToCartCommandRequest.builder()
                .userId(userId)
                .addToCartRequest(addRequest)
                .build();

        // Mock product service response
        ProductDetailsResponse productDetails = ProductDetailsResponse.builder()
                .id("p2")
                .name("Product 2")
                .price(BigDecimal.TEN)
                .build();
        when(productServiceClient.getProductById("p2")).thenReturn(productDetails);

        Cart existingCart = Cart.builder()
                .userId(userId)
                .items(new ArrayList<>())
                .build();

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(existingCart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CartResponse result = addToCartCommand.execute(request);

        assertEquals(1, result.getItems().size());
        assertEquals("p2", result.getItems().get(0).getProductId());
        verify(productServiceClient).getProductById("p2");
    }

    @Test
    void addToCart_ExistingItem_UpdateQuantity_Success() {
        UUID userId = UUID.randomUUID();
        AddToCartRequest addRequest = new AddToCartRequest();
        addRequest.setProductId("p1");
        addRequest.setQuantity(2);

        AddToCartCommandRequest request = AddToCartCommandRequest.builder()
                .userId(userId)
                .addToCartRequest(addRequest)
                .build();

        // Mock product service response
        ProductDetailsResponse productDetails = ProductDetailsResponse.builder()
                .id("p1")
                .name("Product 1")
                .price(BigDecimal.TEN)
                .build();
        when(productServiceClient.getProductById("p1")).thenReturn(productDetails);

        CartItem existingItem = CartItem.builder()
                .productId("p1")
                .quantity(1)
                .price(BigDecimal.TEN)
                .build();
        Cart existingCart = Cart.builder()
                .userId(userId)
                .items(new ArrayList<>())
                .build();
        existingCart.addItem(existingItem);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(existingCart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CartResponse result = addToCartCommand.execute(request);

        assertEquals(1, result.getItems().size());
        assertEquals(3, result.getItems().get(0).getQuantity());
        verify(productServiceClient).getProductById("p1");
    }

    @Test
    void getCart_Success() {
        UUID userId = UUID.randomUUID();
        GetCartRequest request = GetCartRequest.builder().userId(userId).build();

        Cart cart = Cart.builder().userId(userId).build();
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        CartResponse result = getCartCommand.execute(request);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
    }

    @Test
    void getCart_NotFound_ReturnsEmptyCart() {
        UUID userId = UUID.randomUUID();
        GetCartRequest request = GetCartRequest.builder().userId(userId).build();

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        CartResponse result = getCartCommand.execute(request);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void removeFromCart_Success() {
        UUID userId = UUID.randomUUID();
        String productId = "p1";

        RemoveFromCartRequest request = RemoveFromCartRequest.builder()
                .userId(userId)
                .productId(productId)
                .build();

        CartItem item = CartItem.builder().productId(productId).price(BigDecimal.TEN).quantity(1).build();
        Cart cart = Cart.builder().userId(userId).items(new ArrayList<>()).build();
        cart.addItem(item);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CartResponse result = removeFromCartCommand.execute(request);

        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void removeFromCart_CartNotFound_ThrowsException() {
        UUID userId = UUID.randomUUID();
        String productId = "p1";

        RemoveFromCartRequest request = RemoveFromCartRequest.builder()
                .userId(userId)
                .productId(productId)
                .build();

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () -> removeFromCartCommand.execute(request));
    }
}
