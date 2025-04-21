package com.E_commerce.Helper;

public class SellerAlreadyExistsException extends RuntimeException{

    public SellerAlreadyExistsException(String message) {
        super(message);
    }
}
