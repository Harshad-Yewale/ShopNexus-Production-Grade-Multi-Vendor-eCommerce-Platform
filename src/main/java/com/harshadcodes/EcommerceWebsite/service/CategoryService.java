package com.harshadcodes.EcommerceWebsite.service;

import com.harshadcodes.EcommerceWebsite.model.Category;
import com.harshadcodes.EcommerceWebsite.payload.CategoryDTO;
import com.harshadcodes.EcommerceWebsite.payload.CategoryResponse;

public interface CategoryService {

     CategoryResponse listAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);

     CategoryDTO addCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(long categoryId, CategoryDTO updatedCategory);

    CategoryDTO deleteCategory(long categoryId);
}
