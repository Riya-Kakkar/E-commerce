package com.E_commerce.Model;

import jakarta.validation.constraints.Min;

public record CartRemoveDTO (
        @Min(1)
        int userId,
        @Min(1)
        int productId
){
}
