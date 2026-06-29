package com.scaler.myfirstspringbootproj.Controller;


import com.scaler.myfirstspringbootproj.DTO.CreateProductRequestDto;
import com.scaler.myfirstspringbootproj.DTO.UpdateProductRequestDto;
import com.scaler.myfirstspringbootproj.ExceptionHandling.ProductNotfoundException;
import com.scaler.myfirstspringbootproj.Service.ProductService;
import com.scaler.myfirstspringbootproj.models.Product;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fkstoreapi/products")
public class fakestoreproductController {

    private ProductService productService;
    public fakestoreproductController(@Qualifier("fakestoreproductservice") ProductService productService) {
        this.productService = productService;

    }

    @PostMapping
    //input from Client/FE/User here
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequestDto requestDto) {
        Product p = productService.createProduct(requestDto);
        return new ResponseEntity<>(p,HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        ResponseEntity<List<Product>> response = new ResponseEntity<>(products, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) throws ProductNotfoundException {

        Product p=productService.getSingleProduct(id);

        ResponseEntity<Product> responseEntity = new ResponseEntity<>(p,HttpStatus.OK);

        return responseEntity;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> UpdateProduct(@PathVariable("id") Long id, @RequestBody UpdateProductRequestDto updateProductRequestDto) throws ProductNotfoundException {

        Product p=productService.UpdateProduct(id, updateProductRequestDto);

                return new ResponseEntity<>(p,HttpStatus.OK);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> PatchProduct(@PathVariable("id") Long id, @RequestBody UpdateProductRequestDto PatchProductRequestDto) throws ProductNotfoundException {

        Product p=productService.UpdateProduct(id, PatchProductRequestDto);

        return new ResponseEntity<>(p,HttpStatus.OK);

    }
@DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") Long id) throws ProductNotfoundException {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
