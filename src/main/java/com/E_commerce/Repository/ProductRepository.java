package com.E_commerce.Repository;

import com.E_commerce.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Page<Product> findByCategoryAndPriceBetween(String category, Long minPrice, Long maxPrice, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseAndPriceBetween(String name, Long minPrice, Long maxPrice, Pageable pageable);

    Page<Product> findByStock(Integer stock, Pageable pageable);

    Page<Product> findByPriceBetween(Long minPrice, Long maxPrice, Pageable pageable);



}
