package com.E_commerce.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AdminDashboardDTO (
        @NotNull
        @Min(0)
        long totalUsers,
        @NotNull
        @Min(0)
        long totalProducts,
        @NotNull
        @Min(0)
        long totalOrders,
        @NotNull
        @Min(0)
        double totalRevenue
) {
}

