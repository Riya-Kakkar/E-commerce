package com.E_commerce.Helper;

public class OrderNotFoundException extends  RuntimeException{
    public OrderNotFoundException(String message){
        super(message);
    }
}
