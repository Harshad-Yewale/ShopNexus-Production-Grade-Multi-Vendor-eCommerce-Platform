package com.harshadcodes.EcommerceWebsite.service;

import com.harshadcodes.EcommerceWebsite.payload.CartDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity) throws Exception;

    List<CartDTO> getAllCarts() throws Exception;

    CartDTO getUserCart() throws  Exception;

    @Transactional
    CartDTO updateUserCart(@Valid Long productId, @Valid String operation) throws Exception;

    @Transactional
    String deleteCartItem(@Valid Long productId, @Valid Long cartId);

    @Transactional
    boolean updateProductInSideCart(@Valid Long productId, @Valid Long cartId) throws Exception;
}
