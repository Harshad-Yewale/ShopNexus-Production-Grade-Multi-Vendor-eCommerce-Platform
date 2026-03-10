package com.harshadcodes.EcommerceWebsite.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private String productName;
    private String productDescription;
    private String productImage;
    private Integer productQuantity;
    private Double productPrice;
    private Double productDiscount;
    private Double productDiscountedPrice;

}
