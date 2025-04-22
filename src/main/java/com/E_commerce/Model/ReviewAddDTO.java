package com.E_commerce.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ReviewAddDTO(

        @Min(1)
        int userId,
        @Min(1)
        int productId,
        @Min(1)
        int rating,
        @NotBlank(message = "Comment cannot be empty")
        String comment
) {
}
