package com.scaler.myfirstspringbootproj.ExceptionHandling;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
