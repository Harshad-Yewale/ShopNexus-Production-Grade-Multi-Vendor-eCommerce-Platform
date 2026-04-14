package com.harshadcodes.EcommerceWebsite.controller;


import com.harshadcodes.EcommerceWebsite.payload.CartDTO;

import com.harshadcodes.EcommerceWebsite.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
                                                    @PathVariable Integer quantity) throws Exception {
        CartDTO cartDTO=cartService.addProductToCart(productId,quantity);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartDTO);

    }

    @GetMapping
    public ResponseEntity<List<CartDTO>> getAllCarts() throws Exception {
        List<CartDTO> cartDTO=cartService.getAllCarts();
        return ResponseEntity.status(HttpStatus.CREATED).body(cartDTO);

    }

    @GetMapping("/users/cart")
    public ResponseEntity<CartDTO> getUserCart() throws Exception {
        CartDTO cartDTO = cartService.getUserCart();
        return ResponseEntity.status(HttpStatus.CREATED).body(cartDTO);

    }

    @PutMapping("/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateUserCart(@PathVariable @Valid Long productId,
                                                  @PathVariable @Valid String operation) throws Exception{
        CartDTO cartDTO=cartService.updateUserCart(productId,operation);
        return ResponseEntity.status(HttpStatus.OK).body(cartDTO);
    }

    @DeleteMapping("/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteCartItem(@PathVariable @Valid Long productId,
                                                  @PathVariable @Valid Long cartId) throws Exception{
        String message=cartService.deleteCartItem(productId,cartId);

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
