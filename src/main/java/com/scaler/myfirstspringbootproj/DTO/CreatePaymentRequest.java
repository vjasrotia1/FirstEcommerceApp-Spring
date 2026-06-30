package com.scaler.myfirstspringbootproj.DTO;


import com.scaler.myfirstspringbootproj.models.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentRequest {

    private Long OrderId;
    private PaymentMethod paymentMethod;

}
