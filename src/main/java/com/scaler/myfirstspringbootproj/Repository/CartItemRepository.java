package com.scaler.myfirstspringbootproj.Repository;

import com.scaler.myfirstspringbootproj.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {


}
