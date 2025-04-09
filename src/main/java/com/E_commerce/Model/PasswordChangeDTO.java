package com.E_commerce.Model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordChangeDTO (

        @Column(unique = true)
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@(gmail|yahoo|outlook)\\.com$" , message = "Invalid Email !! ")
        String email,
        @NotBlank(message = "Password cannot be empty!!")
        String password

){
}
