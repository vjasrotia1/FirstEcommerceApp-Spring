package com.scaler.myfirstspringbootproj.ExceptionHandling;

public class ProductNotFoundException extends Exception{


    public ProductNotFoundException(String message) {
        super(message);
    }
}
