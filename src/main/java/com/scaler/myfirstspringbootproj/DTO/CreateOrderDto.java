package com.scaler.myfirstspringbootproj.DTO;


import com.stripe.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDto {

    //private Long userId;
    private Long cartId;
    private Long shippingAddressId;
    private PaymentMethod PaymentMethod;
    private String couponCode;

}
