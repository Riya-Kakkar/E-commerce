package com.E_commerce.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record OrderUpdateStatus (
        @Min(1)
        int orderId,
        @NotBlank(message = " Status is required")
        String status
) {
}
