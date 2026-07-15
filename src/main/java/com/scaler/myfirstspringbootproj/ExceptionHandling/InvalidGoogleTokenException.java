package com.scaler.myfirstspringbootproj.ExceptionHandling;

public class InvalidGoogleTokenException extends RuntimeException {
    public InvalidGoogleTokenException(String message) {
        super(message);
    }
}
