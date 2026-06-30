package com.scaler.myfirstspringbootproj.DTO;


import com.scaler.myfirstspringbootproj.models.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {

    private Long orderId;
    private String paymentUrl;
    private String paymentId;
    private Double amount;
    private PaymentStatus paymentStatus;
}
