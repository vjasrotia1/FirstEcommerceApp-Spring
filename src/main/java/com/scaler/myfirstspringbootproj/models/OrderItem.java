package com.scaler.myfirstspringbootproj.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class OrderItem extends BaseModel {

    @ManyToOne
    private Order order;
//one product can be part of many orderItems, but 1 orderItem can relate to one product only
    @ManyToOne
    private Product product;

    private Integer quantity;

    private Double priceAtpurchase;

    //now when the buyer buys iphone, qty=2, price=70000
    //u save order {id: 101, amount = 140000}
    //orderitem :
    //orderid =101, productid=1 qty=2, price =70000
}
