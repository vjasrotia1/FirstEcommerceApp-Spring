package com.scaler.myfirstspringbootproj.ExceptionHandling;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException(String message) {
        super(message);
    }
}
