package com.harshadcodes.EcommerceWebsite.service;
import com.harshadcodes.EcommerceWebsite.exceptions.ResourceAlreadyExistException;
import com.harshadcodes.EcommerceWebsite.exceptions.ResourceNotFoundException;
import com.harshadcodes.EcommerceWebsite.model.Category;
import com.harshadcodes.EcommerceWebsite.payload.CategoryDTO;
import com.harshadcodes.EcommerceWebsite.payload.CategoryResponse;
import com.harshadcodes.EcommerceWebsite.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;


    @Override
    public CategoryResponse listAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {

        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> categoryPage=categoryRepository.findAll(pageDetails);


        List<Category> categoryList=categoryPage.getContent();
        List<CategoryDTO> categoryDTOS= categoryList.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();
        return new CategoryResponse(
                categoryDTOS,
                categoryPage.getNumber(),
                categoryPage.getSize(),
                categoryPage.getTotalElements(),
                categoryPage.getTotalPages(),
                categoryPage.isLast());
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        Category category=modelMapper.map(categoryDTO, Category.class);
        if(categoryRepository.existsByCategoryName(category.getCategoryName())){
            throw new ResourceAlreadyExistException("Category","categoryName", categoryDTO.getCategoryName());
        }
        Category saved=categoryRepository.save(category);
        return modelMapper.map(saved, CategoryDTO.class);

    }

    @Override
    public CategoryDTO updateCategory(long categoryId, CategoryDTO updatedCategory) {
        Category category=getCategoryById(categoryId);
        category.setCategoryName(updatedCategory.getCategoryName());
        Category updated=categoryRepository.save(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(long categoryId) {
        Category category= getCategoryById(categoryId);
        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);

    }

    private Category getCategoryById(long id){
        return categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category","categoryId",id));
    }
}
