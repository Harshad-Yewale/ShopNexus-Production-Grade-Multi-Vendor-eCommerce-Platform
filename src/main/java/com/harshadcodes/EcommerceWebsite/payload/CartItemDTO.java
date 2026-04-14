package com.harshadcodes.EcommerceWebsite.payload;

public record CartItemDTO(
         Long cartItemId,
         CartDTO cart,
         ProductDTO productDTO,
         Integer quantity,
         Double discount,
         Double productPrice
) {
}
