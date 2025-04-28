package com.E_commerce.Model;

import com.E_commerce.Entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public record ProductRespDTO(
        String message,
        List<Product> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {
}
