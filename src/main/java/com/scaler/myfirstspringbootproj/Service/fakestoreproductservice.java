package com.scaler.myfirstspringbootproj.Service;


import com.scaler.myfirstspringbootproj.DTO.CreateProductRequestDto;
import com.scaler.myfirstspringbootproj.DTO.FakeStoreProductDto;
import com.scaler.myfirstspringbootproj.DTO.UpdateProductRequestDto;
import com.scaler.myfirstspringbootproj.ExceptionHandling.ProductNotFoundException;
import com.scaler.myfirstspringbootproj.Repository.CategoryRepository;
import com.scaler.myfirstspringbootproj.Repository.ProductRepository;

import com.scaler.myfirstspringbootproj.models.Category;
import com.scaler.myfirstspringbootproj.models.Product;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("fakestoreproductservice")
public class fakestoreproductservice implements ProductService {

    private RestTemplate restTemplate;
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;



    public fakestoreproductservice(RestTemplate restTemplate, CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.restTemplate = restTemplate;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }


    @Override
    public Product createProduct(CreateProductRequestDto requestDto) {
        //1. Prepare mirror payload structure matching FakeStore API specifications
        //here below, we are creating a new object of type "FakeStoreProductDto" which is a mirror
        //to the JSON format expected by external FKStore API server
        //and setting its value as per what we have received from CLIENT
        FakeStoreProductDto fkpdnewproduct=new FakeStoreProductDto();
        fkpdnewproduct.setTitle(requestDto.getTitle());
        fkpdnewproduct.setDescription(requestDto.getDescription());
        fkpdnewproduct.setPrice(requestDto.getPrice());
        fkpdnewproduct.setImage(requestDto.getImageUrl());
        fkpdnewproduct.setCategory(requestDto.getCategoryName());

        //2. perform the external network post Transaction
        FakeStoreProductDto response=restTemplate.postForObject(
                "https://fakestoreapi.com/products",
                fkpdnewproduct,FakeStoreProductDto.class
        );

        if(response==null){
            throw new RuntimeException("External Fakestore API failed to process new Product creation request");
        }
//3. Convert FakeStore response into your native internal Product object layout
        Product nativeProduct= response.getProduct();

        //4. complete logic for category verification check whether it exists in our DB or not
        String targetcategoryName = requestDto.getCategoryName();

        //5. check if category already exists in your local database
        Optional<Category> existingCategory = categoryRepository.findByName(targetcategoryName);

        Category finalCategory;
        if(existingCategory.isPresent()){
            //category exists already, so resuse the exact from ur database
            finalCategory=existingCategory.get();
        }
        else{
            //create a new record, populate it and save it
            Category newCat=new Category();
            newCat.setName(targetcategoryName);
            finalCategory= categoryRepository.save(newCat);
        }

        //5. linking the existing or newly saved category to your native product
        nativeProduct.setCategory(finalCategory);
//6. Synchronise state by persisting the final structured record inside your DB
        return productRepository.save(nativeProduct);
    }

    @Override
    public Product getSingleProduct(Long id) throws ProductNotFoundException {
        FakeStoreProductDto fkpd=restTemplate.getForObject(
                "https://fakestoreapi.com/products/"+id,FakeStoreProductDto.class
        );

        if(fkpd==null){
            throw new ProductNotFoundException("Product not found with id "+id);
        }
        return fkpd.getProduct();
    }

    @Override
    public Page<Product> getAllProducts(int pageNumber, int pageSize, String fieldName) {
        List<Product> products=new ArrayList<>();
        FakeStoreProductDto[] fakeStoreProductDtoarray = restTemplate.getForObject(
                "https://fakestoreapi.com/products",FakeStoreProductDto[].class);

        if(fakeStoreProductDtoarray!=null){
            for(FakeStoreProductDto fkspdto : fakeStoreProductDtoarray){
                products.add(fkspdto.getProduct());
            }
        }
        return null;
    }

    @Override //PUT request change all the details of a given product
    public Product UpdateProduct(Long id, UpdateProductRequestDto updateProductRequestDto) throws ProductNotFoundException {

        Product existingProduct=this.getSingleProduct(id);

//1. map incoming update request fields to external API mirror DTO Structure
        FakeStoreProductDto fkpdupdatedpayLoad=new FakeStoreProductDto();

        fkpdupdatedpayLoad.setCategory(updateProductRequestDto.getCategory());
        fkpdupdatedpayLoad.setTitle(updateProductRequestDto.getTitle());
        fkpdupdatedpayLoad.setDescription(updateProductRequestDto.getDescription());
        fkpdupdatedpayLoad.setPrice(updateProductRequestDto.getPrice());
        fkpdupdatedpayLoad.setImage(updateProductRequestDto.getImage());

        //2. Wrap the payload into an HttpEntity container required by exchange()
        HttpEntity<FakeStoreProductDto> requestEntity=new HttpEntity<>(fkpdupdatedpayLoad);
        //3. make external Network PUT Call to Fakestore API

        ResponseEntity<FakeStoreProductDto> responseEntity=restTemplate.exchange(
                "https://fakestoreapi.com/products/"+id,
                HttpMethod.PUT,
                requestEntity,
                FakeStoreProductDto.class
        );

        FakeStoreProductDto updatedfkstoreproduct=responseEntity.getBody();
        if(updatedfkstoreproduct==null){
            throw new ProductNotFoundException("Product with "+ id+ " could not be replaced");
        }

        String targetCategoryName = updateProductRequestDto.getCategory();

        Category finalCategory= categoryRepository.findByName(targetCategoryName)
                .orElseGet(() -> {
                    Category newCat=new Category();
                    newCat.setName(targetCategoryName);
                    return categoryRepository.save(newCat);
                });

        existingProduct.setCategory(finalCategory);
        existingProduct.setTitle(updatedfkstoreproduct.getTitle());
        existingProduct.setDescription(updatedfkstoreproduct.getDescription());
        existingProduct.setPrice(updatedfkstoreproduct.getPrice());
        existingProduct.setImageUrl(updatedfkstoreproduct.getImage());
        //existingProduct.setId(id);

        return productRepository.save(existingProduct);

    }

    @Override
    public Product PatchProduct(Long id, UpdateProductRequestDto PatchProductRequestDto) throws ProductNotFoundException {

        //step 1 : existence check, reuse getSingleProduct() method
        //if product doesnot exist, it throws productnotfoundexception and stops further execution
        Product existingProd=this.getSingleProduct(id);

        //step 2 : sparse payload preparation(only pack the fields provided by user)
        FakeStoreProductDto patchPayload=new FakeStoreProductDto();
        if(PatchProductRequestDto.getTitle()!=null){
            patchPayload.setTitle(PatchProductRequestDto.getTitle());
            existingProd.setTitle(patchPayload.getTitle());
        }
        if(PatchProductRequestDto.getDescription()!=null){
            patchPayload.setDescription(PatchProductRequestDto.getDescription());
            existingProd.setDescription(patchPayload.getDescription());
        }
        if(PatchProductRequestDto.getPrice()!=null){
            patchPayload.setPrice(PatchProductRequestDto.getPrice());
            existingProd.setPrice(patchPayload.getPrice());
        }
        if(PatchProductRequestDto.getImage()!=null){
            patchPayload.setImage(PatchProductRequestDto.getImage());
            existingProd.setImageUrl(patchPayload.getImage());
        }

        //step 3 : dynamic category assignment
        if(PatchProductRequestDto.getCategory()!=null) {
            patchPayload.setCategory(PatchProductRequestDto.getCategory());

            String targetCategoryName = PatchProductRequestDto.getCategory();
            Category verifiedCategory = categoryRepository.findByName(targetCategoryName)
                    .orElseGet(() -> {
                        Category newCat = new Category();
                        newCat.setName(targetCategoryName);
                        return categoryRepository.save(newCat);
                    });
            existingProd.setCategory(verifiedCategory);
        }
        //step 4 : external network dispatch (using HTTP patch)
        HttpEntity<FakeStoreProductDto> entitywrapper=new HttpEntity<>(patchPayload);
        ResponseEntity<FakeStoreProductDto> response = restTemplate.exchange(
                "https://fakestoreapi.com/products/"+id,
                HttpMethod.PATCH,
                entitywrapper,
                FakeStoreProductDto.class
        );
        if(response.getBody()==null){
            throw new RuntimeException("3rd Party failed to process partial updates with id "+id);
        }
        //step 5 : save to local repo
        //explicitly maintain the request path id to lock relational consistency
            existingProd.setId(id);
        return productRepository.save(existingProd);
    }

    @Override
    public void deleteProduct(Long id) throws ProductNotFoundException {
        //if product is not in fakestore, throw error
            getSingleProduct(id);
            restTemplate.delete("https://fakestoreapi.com/products/"+id);
    }


}
