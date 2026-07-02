package com.scaler.myfirstspringbootproj.ExceptionHandling;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
