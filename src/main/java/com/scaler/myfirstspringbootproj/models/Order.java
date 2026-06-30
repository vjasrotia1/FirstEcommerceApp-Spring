package com.scaler.myfirstspringbootproj.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
//after ordering you need to save:
//
//Product name
//Price at purchase time
//Quantity
//Discount

//order item stores snapshot
//initial order status is "CREATED", after STRIPE payment success status should be "PAID"
@Table(name = "orders")
public class Order extends  BaseModel{

    private  Double amount;
    //private String status;

@ManyToOne
    private User user;

//problem with this:
    //suppose customer buys iphone qty=2, price at the time of purchase=70000
    //but with List<Product> products u will only store Order---> iphone
    //where do u save quantity=2,price at time of purchase,discount? etc
    //product price can change tommorrow


@OneToMany(
        mappedBy = "order",
        cascade=CascadeType.ALL
)
private List<OrderItem> orderItems=new ArrayList<>();

    @Enumerated(EnumType.STRING)
private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
private PaymentMethod paymentMethod;

    //why not order:products : M:M cardinality
    //since we will lose information
    //we will only store order_id and Products like 101,iphone
    //what about qty,purchaseprice,discount?
    //This design is exactly what you need for your Stripe payment flow
    // because the amount you send to Stripe should come from Order/OrderItem snapshot,
    // not current Product price

}
