package com.scaler.myfirstspringbootproj.ExceptionHandling;

public class ProductNotfoundException extends Exception{


    public ProductNotfoundException(String message) {
        super(message);
    }
}
