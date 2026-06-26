package com.scaler.myfirstspringbootproj.Service;

import com.scaler.myfirstspringbootproj.DTO.CreateProductRequestDto;
import com.scaler.myfirstspringbootproj.DTO.UpdateProductRequestDto;
import com.scaler.myfirstspringbootproj.ExceptionHandling.ProductNotfoundException;
import com.scaler.myfirstspringbootproj.models.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(CreateProductRequestDto requestDto);

    Product getSingleProduct(Long id) throws ProductNotfoundException;

    List<Product> getAllProducts();
    Product UpdateProduct(Long id, UpdateProductRequestDto updateProductRequestDto) throws ProductNotfoundException;

    Product PatchProduct(Long id, UpdateProductRequestDto PatchProductRequestDto) throws ProductNotfoundException;

    void deleteProduct(Long id) throws ProductNotfoundException;
}
