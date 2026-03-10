package com.harshadcodes.EcommerceWebsite.repositories;

import com.harshadcodes.EcommerceWebsite.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    boolean existsByProductNameAndCategory_CategoryId(String productName, Long categoryId);

    Page<Product> findByCategory_CategoryId(Long categoryId, Pageable pageable);

    Page<Product> findByProductNameContainingIgnoreCase(String s, Pageable pageable);
}
