package com.scaler.myfirstspringbootproj.Service;

import com.scaler.myfirstspringbootproj.DTO.CreateProductRequestDto;
import com.scaler.myfirstspringbootproj.DTO.UpdateProductRequestDto;
import com.scaler.myfirstspringbootproj.ExceptionHandling.ProductAlreadyExistsException;
import com.scaler.myfirstspringbootproj.ExceptionHandling.ProductNotFoundException;
import com.scaler.myfirstspringbootproj.Repository.CategoryRepository;
import com.scaler.myfirstspringbootproj.Repository.ProductRepository;
import com.scaler.myfirstspringbootproj.models.Category;
import com.scaler.myfirstspringbootproj.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;

import java.util.Optional;


@Service("selfproductservice")
public class selfproductservice implements ProductService {

    private final CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    //private RedisTemplate<String, Product> redisTemplate;

    public selfproductservice(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
  //this.redisTemplate = redisTemplate;
        this.categoryRepository = categoryRepository;
    }
    @Override
    //ideally in Real Production, only Admin should have the right to create or delete product
    public Product createProduct(CreateProductRequestDto requestDto) {
        Optional<Product> optionalproduct= productRepository.findByTitle(requestDto.getTitle());

            if(optionalproduct.isPresent()){
                throw new ProductAlreadyExistsException("Product with title " +requestDto.getTitle()+ " already exists");
            }

            //else create this new product in DB

        Product product = new Product();
            product.setTitle(requestDto.getTitle());
            product.setDescription(requestDto.getDescription());
            product.setPrice(requestDto.getPrice());
            product.setImageUrl(requestDto.getImageUrl());

            Category finalCategory=categoryRepository.findByName(requestDto.getCategoryName())
                    .orElseGet(
                            () -> {
                                Category newCategory=new Category();
                                newCategory.setName(requestDto.getCategoryName());
                                return categoryRepository.save(newCategory);
                            }
                    );
            product.setCategory(finalCategory);
        return productRepository.save(product);
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
