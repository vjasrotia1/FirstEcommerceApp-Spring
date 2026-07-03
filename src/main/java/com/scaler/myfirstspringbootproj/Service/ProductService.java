package com.scaler.myfirstspringbootproj.Service;

import com.scaler.myfirstspringbootproj.DTO.CreateProductRequestDto;
import com.scaler.myfirstspringbootproj.DTO.UpdateProductRequestDto;
import com.scaler.myfirstspringbootproj.ExceptionHandling.ProductNotFoundException;
import com.scaler.myfirstspringbootproj.models.Product;
import org.springframework.data.domain.Page;

public interface ProductService {

    Product createProduct(CreateProductRequestDto requestDto);

    Product getSingleProduct(Long id) throws ProductNotFoundException;

    Page<Product> getAllProducts(int pageNumber, int pageSize, String fieldName);
    Product UpdateProduct(Long id, UpdateProductRequestDto updateProductRequestDto) throws ProductNotFoundException;

    Product PatchProduct(Long id, UpdateProductRequestDto PatchProductRequestDto) throws ProductNotFoundException;

    void deleteProduct(Long id) throws ProductNotFoundException;
}
