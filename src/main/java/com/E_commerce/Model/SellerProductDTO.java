package com.E_commerce.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SellerProductDTO(

        @NotNull(message = "Product ID is required for update")
        int productId,

        @NotBlank(message = "Product name is required")
         String name,

        @NotBlank(message = "Description is required")
         String description,

        @NotNull(message = "Price is required")
        @Min(value = 0, message = "Price must be non-negative")
         Long price,

        @NotNull(message = "Stock is required")
        @Min(value = 0, message = "Stock must be non-negative")
         Integer stock,

        @NotBlank(message = "Category is required")
         String category
) {
}
