package com.example.subscription.exception;

public class GlobalExceptionHandler extends RuntimeException{
    public GlobalExceptionHandler(String message){
        super(message);
    }
}
