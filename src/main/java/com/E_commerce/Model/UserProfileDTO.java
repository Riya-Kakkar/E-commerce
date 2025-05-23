package com.E_commerce.Model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserProfileDTO(
        int id,
        @NotBlank(message = "Name can not be empty!! ")
        @Size(min = 2,max = 20,message = "Name must be in between 2-20 characters...")
        String username,
        @Column(unique = true)
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@(gmail|yahoo|outlook)\\.com$" , message = "Invalid Email !! ")
        String email,
        String role,
        boolean enable
) {
}
