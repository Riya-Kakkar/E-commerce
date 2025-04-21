package com.E_commerce.Helper;

public class InvalidCredentialsException extends  RuntimeException{
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
