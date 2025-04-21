package com.E_commerce.Model;

import jakarta.validation.constraints.NotBlank;

public record SellerLoginDTO (

        @NotBlank(message = "Username is required!! ")
         String username,

        @NotBlank(message = "Password cannot be empty!!")
         String password
){
}
