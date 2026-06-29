package com.scaler.myfirstspringbootproj.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "orders")
public class Order extends  BaseModel{

    private  Double amount;
    private String status;

@ManyToOne
    private User user;
@ManyToMany
private List<Product> products;

    @Enumerated(EnumType.STRING)
private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
private PaymentMethod paymentMethod;

    @Override
    public String toString() {
        return "Order{" +
                "amount=" + amount +
                ", status='" + status + '\'' +
                ", user=" + user +
                ", products=" + products +
                ", orderStatus=" + orderStatus +
                ", paymentMethod=" + paymentMethod +
                '}';
    }
}
