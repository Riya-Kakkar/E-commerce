package com.E_commerce.Repository;

import com.E_commerce.Entity.Product;
import com.E_commerce.Entity.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
   List<Product> findBySellerId(int sellerId);
    List<Product> findBySeller(Seller seller);
    Page<Product> findByPriceBetween(long minPrice, long maxPrice, Pageable pageable);
    Page<Product> findByNameContaining(String name, Pageable pageable);
    Page<Product> findByCategory(String category, Pageable pageable);

}
