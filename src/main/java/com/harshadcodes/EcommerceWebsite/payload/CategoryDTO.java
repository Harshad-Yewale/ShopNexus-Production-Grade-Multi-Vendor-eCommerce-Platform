package com.harshadcodes.EcommerceWebsite.payload;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long categoryId;
    @NotBlank
    private String categoryName;



    @Override
    public String toString() {
        return "Category{\n" +
                "   categoryId=" + categoryId +","+"\n"+
                "   categoryName='" + categoryName + '\'' +"\n"+
                '}';
    }
}
