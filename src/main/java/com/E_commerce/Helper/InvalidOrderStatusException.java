package com.E_commerce.Helper;

public class InvalidOrderStatusException extends RuntimeException{
    public InvalidOrderStatusException(String message) {
        super(message);
    }
}
