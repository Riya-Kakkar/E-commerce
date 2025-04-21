package com.E_commerce.Helper;

public class ProductNotFoundException extends  RuntimeException{
    public ProductNotFoundException(String message) {
        super(message);
    }
}
