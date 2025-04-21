package com.E_commerce.Model;

import com.E_commerce.Entity.Product;
import org.springframework.data.domain.Page;

public record ProductRespDTO(
        String message,
        Page<Product> data
)
{
}
