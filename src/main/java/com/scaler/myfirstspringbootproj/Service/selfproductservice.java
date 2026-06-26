package com.scaler.myfirstspringbootproj.Service;

import com.scaler.myfirstspringbootproj.DTO.CreateProductRequestDto;
import com.scaler.myfirstspringbootproj.DTO.UpdateProductRequestDto;
import com.scaler.myfirstspringbootproj.ExceptionHandling.ProductNotfoundException;
import com.scaler.myfirstspringbootproj.models.Product;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("selfproductservice")
public class selfproductservice implements ProductService {
    @Override
    public Product createProduct(CreateProductRequestDto requestDto) {
        return null;
    }

    @Override
    public Product getSingleProduct(Long id) {
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        return List.of();
    }

    @Override
    public Product UpdateProduct(Long id, UpdateProductRequestDto updateProductRequestDto) throws ProductNotfoundException {

        return null;
    }

    @Override
    public Product PatchProduct(Long id, UpdateProductRequestDto PatchProductRequestDto) throws ProductNotfoundException {
        return null;
    }

    @Override
    public void deleteProduct(Long id) throws ProductNotfoundException {

    }
}
