package com.E_commerce.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


public record UserDTO (
        @NotBlank(message = "Name can not be empty!! ")
        @Size(min = 2,max = 20,message = "Name must be in between 2-20 characters...")
         String username,

        @Column(unique = true)
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@(gmail|yahoo|outlook)\\.com$" , message = "Invalid Email !! ")
         String email,

        @NotBlank(message = "Password cannot be empty!!")
        String password,

        String role
)
{
}
