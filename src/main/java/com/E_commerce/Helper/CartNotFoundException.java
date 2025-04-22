package com.E_commerce.Helper;

public class CartNotFoundException extends  RuntimeException{
    public CartNotFoundException(String message) {
        super(message);
    }
}
