package com.harshadcodes.EcommerceWebsite.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "products_table")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank
    private String productName;
    private String productDescription;
    private String productImage;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer productQuantity;

    @NotNull
    @DecimalMin("0.0")
    private Double productPrice;

    @DecimalMin("0.0")
    private Double productDiscount;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user;

    @Transient
    public Double getProductDiscountedPrice() {
        if (productPrice == null) return null;
        if (productDiscount == null) return productPrice;
        return productPrice-(productDiscount *0.01)*productPrice;
    }
}
