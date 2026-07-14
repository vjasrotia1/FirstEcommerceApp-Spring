package com.scaler.myfirstspringbootproj.Controller;


import com.scaler.myfirstspringbootproj.DTO.CreateProductRequestDto;
import com.scaler.myfirstspringbootproj.DTO.ErrorDto;
import com.scaler.myfirstspringbootproj.DTO.TokenValidationResult;
import com.scaler.myfirstspringbootproj.ExceptionHandling.ProductNotFoundException;
import com.scaler.myfirstspringbootproj.ExceptionHandling.UnauthorizedException;
import com.scaler.myfirstspringbootproj.Service.AuthService;
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

    private final AuthService authService;
    private ProductService  productService;

    public SelfProductController(@Qualifier("selfproductservice") ProductService productService, AuthService authService) {
        this.productService = productService;
        this.authService = authService;
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

    @PostMapping
    //suppose - Product can be created only by an ADMIN
    public ResponseEntity<Product> createProduct(@RequestBody  CreateProductRequestDto createProductRequestDto,
                                                 @RequestHeader("Authorization") String token) {

            TokenValidationResult tokenValidationResult = authService.validateToken(token);
            if(!tokenValidationResult.isValid()){
                throw new UnauthorizedException("Invalid or Expired token");

            }
            if(!tokenValidationResult.getRole().equals("ROLE_ADMIN")){
                throw new UnauthorizedException("Only Admin can Create Product");
            }
            Product product=productService.createProduct(createProductRequestDto);
            return new ResponseEntity<>(product, HttpStatus.CREATED);


            //similarly we can make changes in order controller where we can say that only Logged In User can see the orders
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
