package com.E_commerce.Model;

public record UserProfileDTO(
        int id,
        String username,
        String email,
        String role,
        boolean enable
) {
}
