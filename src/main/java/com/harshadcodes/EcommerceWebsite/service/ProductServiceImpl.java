package com.harshadcodes.EcommerceWebsite.service;

import com.harshadcodes.EcommerceWebsite.exceptions.ResourceAlreadyExistException;
import com.harshadcodes.EcommerceWebsite.exceptions.ResourceNotFoundException;
import com.harshadcodes.EcommerceWebsite.model.Category;
import com.harshadcodes.EcommerceWebsite.model.Product;
import com.harshadcodes.EcommerceWebsite.payload.ProductDTO;
import com.harshadcodes.EcommerceWebsite.payload.ProductResponse;
import com.harshadcodes.EcommerceWebsite.repositories.CategoryRepository;
import com.harshadcodes.EcommerceWebsite.repositories.ProductRepository;
import com.harshadcodes.EcommerceWebsite.utils.PaginationUtility;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private  final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final FilesService filesService;

    @Value("${product.image}")
    private String path;

    @Override
    public ProductDTO saveProduct(ProductDTO productDTO, Long categoryId) {
        Category category= categoryRepository.findById(categoryId).orElseThrow(
                ()->new ResourceNotFoundException("Category","CategoryId",categoryId)
        );

        Product  product = modelMapper.map(productDTO, Product.class);
        if(productRepository.existsByProductNameAndCategory_CategoryId(product.getProductName(),category.getCategoryId())){
            throw new ResourceAlreadyExistException("Product","ProductId and Category",product.getProductName()+" - "+category.getCategoryName());
        }
        product.setCategory(category);
        Product savedProduct=productRepository.save(product);
        ProductDTO savedProductDTO=modelMapper.map(savedProduct, ProductDTO.class);
        savedProductDTO.setProductDiscountedPrice(savedProduct.getProductDiscountedPrice());
        return savedProductDTO;
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Pageable pageDetails= PaginationUtility.createPageable(pageNumber,pageSize,sortBy,sortOrder);
        Page<Product> productPage=productRepository.findAll(pageDetails);
        return buildProductResponse(productPage);
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        categoryRepository.findById(categoryId).orElseThrow(() ->
                        new ResourceNotFoundException("Category", "CategoryId", categoryId));

        Pageable pageable = PaginationUtility.createPageable(pageNumber, pageSize, sortBy, sortOrder);

        Page<Product> productPage = productRepository.findByCategory_CategoryId(categoryId, pageable);


        return buildProductResponse(productPage);
    }

    @Override
    public ProductResponse getProductsByKeywords(String keywords,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Pageable pageable=PaginationUtility.createPageable(pageNumber,pageSize,sortBy,sortOrder);
        Page<Product> productPage =productRepository.findByProductNameContainingIgnoreCase(keywords,pageable );
        return buildProductResponse(productPage);
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product","ProductId",productId));

        product.setProductName(productDTO.getProductName());
        product.setProductDescription(productDTO.getProductDescription());
        product.setProductImage(productDTO.getProductImage());
        product.setProductQuantity(productDTO.getProductQuantity());
        product.setProductPrice(productDTO.getProductPrice());
        product.setProductDiscount(productDTO.getProductDiscount());
        productRepository.save(product);
        ProductDTO savedProductDTO=modelMapper.map(product, ProductDTO.class);
        savedProductDTO.setProductDiscountedPrice(product.getProductDiscountedPrice());
        return savedProductDTO;
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product","ProductId",productId));
        productRepository.delete(product);
        ProductDTO productDTO=modelMapper.map(product, ProductDTO.class);
        productDTO.setProductDiscountedPrice(product.getProductDiscountedPrice());
        return productDTO;
    }

    @Override
    public ProductDTO uploadFiles(Long productId, MultipartFile file) throws IOException {
        Product productFromDb=productRepository.findById(productId).orElseThrow(
                ()->new ResourceNotFoundException("Product","ProductId",productId)
        );

        String fileName=filesService.uploadImage(path,file);

        productFromDb.setProductImage(fileName);
        Product savedProduct=productRepository.save(productFromDb);
        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    private   ProductResponse buildProductResponse(Page<Product> productPage) {
        List<ProductDTO> productDTOS = productPage.getContent().stream().map(product ->
        {
            ProductDTO dto = modelMapper.map(product, ProductDTO.class);
            dto.setProductDiscountedPrice(product.getProductDiscountedPrice());
            return dto;
        }).toList();
        return new ProductResponse(
                productDTOS,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast());
    }

}
