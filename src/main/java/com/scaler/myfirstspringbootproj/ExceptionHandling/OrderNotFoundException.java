package com.scaler.myfirstspringbootproj.ExceptionHandling;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String message) {
        super(message);
    }
}
