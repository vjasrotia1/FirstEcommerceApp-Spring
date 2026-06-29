package com.scaler.myfirstspringbootproj.DTO;

import com.scaler.myfirstspringbootproj.models.User;
import com.stripe.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//kind of DTO class for OrderService- decoupled from whatever input we are getting from FrontEnd
//create a new order for this given userId and cartId
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class createNewOrderRequest {

    private Long userId;
    private Long cartId;
    private Long shippingAddressId;
    private PaymentMethod PaymentMethod;
    private String couponCode;

}
