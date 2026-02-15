package com.harshadcodes.EcommerceWebsite.controller;

import com.harshadcodes.EcommerceWebsite.constants.AppConstants;
import com.harshadcodes.EcommerceWebsite.model.Category;
import com.harshadcodes.EcommerceWebsite.payload.CategoryDTO;
import com.harshadcodes.EcommerceWebsite.payload.CategoryResponse;
import com.harshadcodes.EcommerceWebsite.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class CategoryController {
    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber",required = false,defaultValue = AppConstants.PAGE_NUMBER)Integer pageNumber,
            @RequestParam(name = "pageSize",required = false,defaultValue = AppConstants.PAGE_SIZE)Integer pageSize,
            @RequestParam(name = "sortBy",required = false,defaultValue = AppConstants.SORT_BY)String sortBy,
            @RequestParam(name = "sortOrder",required = false,defaultValue = AppConstants.SORT_ORDER)String sortOrder
                                                             ) {
       CategoryResponse categoryResponse=categoryService.listAllCategories(pageNumber,pageSize,sortBy,sortOrder);

        return ResponseEntity.ok(categoryResponse);
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
            CategoryDTO saved=categoryService.addCategory(categoryDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @PathVariable("categoryId") long categoryId, @Valid @RequestBody CategoryDTO updatedCategoryDTO) {
          CategoryDTO updated= categoryService.updateCategory(categoryId, updatedCategoryDTO);
                   return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@Valid @PathVariable("categoryId") long categoryId) {
        CategoryDTO deleted=categoryService.deleteCategory(categoryId);
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/public/echo")
    public ResponseEntity<String> echoMessage(
            @RequestParam(name = "message",required = false,defaultValue = "Hello") String message){
        return ResponseEntity.ok("Message : "+message);
    }
}





