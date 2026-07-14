package com.scaler.myfirstspringbootproj.ExceptionHandling;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String message) {
        super(message);
    }
}
