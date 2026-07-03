package com.scaler.myfirstspringbootproj.Controller;


import com.scaler.myfirstspringbootproj.DTO.ErrorDto;
import com.scaler.myfirstspringbootproj.ExceptionHandling.ProductNotFoundException;
import com.scaler.myfirstspringbootproj.Service.ProductService;
import com.scaler.myfirstspringbootproj.Service.selfproductservice;
import com.scaler.myfirstspringbootproj.models.Product;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class SelfProductController {

    private ProductService  productService;

    public SelfProductController(@Qualifier("selfproductservice") ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getSingleProduct(@PathVariable("id") Long id) throws ProductNotFoundException {

        Product product=productService.getSingleProduct(id);
        return new  ResponseEntity<>(product, HttpStatus.OK);
    }

        @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("fieldName") String fieldName) {

            return new ResponseEntity<>(productService.getAllProducts(pageNumber,pageSize,fieldName), HttpStatus.OK);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handleProductNotFoundException(Exception e){
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setMessage(e.getMessage());

        ResponseEntity<ErrorDto> res;
        res = new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);

        return res;
    }

}
