package com.scaler.myfirstspringbootproj.Repository;

import com.scaler.myfirstspringbootproj.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {


    List<Order> findAllByUser_Id(Long userId);
}
