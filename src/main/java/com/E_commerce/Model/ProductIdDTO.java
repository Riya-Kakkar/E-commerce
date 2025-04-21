package com.E_commerce.Model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductIdDTO(
        @NotNull(message = "Product ID is required")
        @Positive(message = "Product ID must be a positive number")
        int productId
) {
}
