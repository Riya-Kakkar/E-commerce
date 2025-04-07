package com.E_commerce.Helper;

public class UserNotFoundException extends  RuntimeException{

    public UserNotFoundException(String message) {
        super(message);
    }
}
