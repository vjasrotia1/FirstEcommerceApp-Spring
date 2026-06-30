package com.scaler.myfirstspringbootproj.ExceptionHandling;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String message) {
        super(message);
    }
}
