package com.scaler.myfirstspringbootproj.Repository;

import com.scaler.myfirstspringbootproj.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    //@Override
    //Page<Product> findAll(Pageable pageable);
    //no need to declare it again unless u want to customise it as JPA repository already provides this method

    //this will insert records in my product table in my DB
    Product save(Product product);


    //hibernate will write queries on our behalf
    //as hibernate is ORM provider which underlying implements JPA and my product repo is child of JPA Repo
    // query : select * from product where title=title;
    Optional<Product> findByTitle(String title);

    Optional<Product> findByDescription(String description);

    @Override
    List<Product> findAll();
}
