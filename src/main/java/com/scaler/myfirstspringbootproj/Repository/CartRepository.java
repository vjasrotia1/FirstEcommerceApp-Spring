package com.scaler.myfirstspringbootproj.Repository;

import com.scaler.myfirstspringbootproj.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
