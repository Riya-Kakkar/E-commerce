package com.E_commerce.Model;

import jakarta.validation.constraints.Min;

public record CartAddDTO (
        @Min(1)
        int userId,
        @Min(1)
        int productId,
        @Min(1)
        int quantity
){
}
