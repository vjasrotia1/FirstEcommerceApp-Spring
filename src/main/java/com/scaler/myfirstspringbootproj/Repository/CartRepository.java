package com.scaler.myfirstspringbootproj.Repository;

import com.scaler.myfirstspringbootproj.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
Optional<Cart> findByUserId(Long userId);
Optional<Cart> findById(Long id);
}
