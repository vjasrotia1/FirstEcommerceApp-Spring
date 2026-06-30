package com.scaler.myfirstspringbootproj.DTO;


import com.scaler.myfirstspringbootproj.models.PaymentMethod;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentDto {

    private Long orderId;
    private PaymentMethod paymentMethod;

}
