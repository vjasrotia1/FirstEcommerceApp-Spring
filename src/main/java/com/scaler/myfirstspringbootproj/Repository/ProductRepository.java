package com.scaler.myfirstspringbootproj.Repository;

import com.scaler.myfirstspringbootproj.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    //this will insert records in my product table in my DB
    Product save(Product product);


    //hibernate will write queries on our behalf
    //as hibernate is ORM provider which underlying implements JPA and my product repo is child of JPA Repo
    // query : select * from product where title=title;
    Product findByTitle(String title);

    Product findByDescription(String description);

    @Override
    List<Product> findAll();
}
