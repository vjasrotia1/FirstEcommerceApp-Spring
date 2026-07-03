package com.scaler.myfirstspringbootproj.Service;

import com.scaler.myfirstspringbootproj.DTO.CreateProductRequestDto;
import com.scaler.myfirstspringbootproj.DTO.UpdateProductRequestDto;
import com.scaler.myfirstspringbootproj.ExceptionHandling.ProductNotFoundException;
import com.scaler.myfirstspringbootproj.Repository.ProductRepository;
import com.scaler.myfirstspringbootproj.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;


@Service("selfproductservice")
public class selfproductservice implements ProductService {

    private ProductRepository productRepository;
    //private RedisTemplate<String, Product> redisTemplate;

    public selfproductservice(ProductRepository productRepository) {
        this.productRepository = productRepository;
  //this.redisTemplate = redisTemplate;

    }
    @Override
    public Product createProduct(CreateProductRequestDto requestDto) {
        return null;
    }

    @Override
    public Product getSingleProduct(Long id) throws ProductNotFoundException {
//        String key= "product-"+id;
//        Product product =  (Product) redisTemplate.opsForValue().get(key);
//        if(product!=null){
//            System.out.println("fetched from redis");
//            return product;
//        }
//
//        System.out.println("fetched from my database");
//
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("product not found in DB"));
//
//        redisTemplate.opsForValue().set(key,product);
//
        return product;

    }

    @Override
    public Page<Product> getAllProducts(int pageNumber, int pageSize, String fieldName) {
        Page<Product> products= productRepository.findAll(
                PageRequest.of(pageNumber,pageSize, Sort.by(fieldName).ascending())
        );
        return products;
    }

    @Override
    public Product UpdateProduct(Long id, UpdateProductRequestDto updateProductRequestDto) throws ProductNotFoundException {

        return null;
    }

    @Override
    public Product PatchProduct(Long id, UpdateProductRequestDto PatchProductRequestDto) throws ProductNotFoundException {
        return null;
    }

    @Override
    public void deleteProduct(Long id) throws ProductNotFoundException {

    }
}
