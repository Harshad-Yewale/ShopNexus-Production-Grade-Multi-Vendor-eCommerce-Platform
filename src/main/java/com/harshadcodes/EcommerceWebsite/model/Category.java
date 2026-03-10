package com.harshadcodes.EcommerceWebsite.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Categories_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @NotBlank
    private String categoryName;


    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    List<Product> products;



    @Override
    public String toString() {
        return "Category{\n" +
                "   categoryId=" + categoryId +","+"\n"+
                "   categoryName='" + categoryName + '\'' +"\n"+
                '}';
    }
}
