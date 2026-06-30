package com.scaler.myfirstspringbootproj.Service;


import com.scaler.myfirstspringbootproj.DTO.CreatePaymentRequest;
import com.scaler.myfirstspringbootproj.DTO.PaymentResponseDto;
import com.scaler.myfirstspringbootproj.Repository.OrderRepository;
import com.stripe.exception.StripeException;

public interface PaymentService {

PaymentResponseDto createPayment(CreatePaymentRequest createPaymentRequest) throws StripeException;
}
