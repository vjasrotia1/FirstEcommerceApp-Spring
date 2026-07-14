package com.scaler.myfirstspringbootproj.models;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

//one product can be in many different user's carts as cart item
//but one cart item can only be one given product so that is why manytoOne relationship
//for one user there will only be one cart
//but one product can be a part of many carts as cartItem
//this is the standard ecommerce cart design

public class CartItem extends BaseModel {

    @ManyToOne
    private Cart cart;
    //one product can be part of many cartItems, but 1 cartItem can relate to one product only
    @ManyToOne
    private Product product;

    private Integer quantity;

    }
