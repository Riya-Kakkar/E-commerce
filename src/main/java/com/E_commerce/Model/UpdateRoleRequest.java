package com.E_commerce.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateRoleRequest (
        @NotBlank(message = "Role must not be blank")
        @Pattern(regexp = "^(ADMIN|CUSTOMER|SELLER)$", message = "Role must be one of: ADMIN, CUSTOMER, SELLER")
        String role
){
}
