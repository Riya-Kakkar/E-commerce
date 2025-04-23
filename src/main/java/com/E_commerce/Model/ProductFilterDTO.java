package com.E_commerce.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProductFilterDTO(
        String name,
        String category,


         Long priceMin,

         Long priceMax,

        @Min(value = 0, message = "Stock must be 0 or more")
        Integer stock,

        Integer page,

        @Min(value = 5, message = "Page size must be at least 5")
        Integer size
) {

    public ProductFilterDTO {
        if (page == null || page < 0) {
            page = 0;
        }
        if (size == null || size <= 5) {
            size = 10;
        }
    }

}
