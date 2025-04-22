package com.E_commerce.Repository;

import com.E_commerce.Entity.Product;
import com.E_commerce.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByProductId(int productId);
    List<Review> findByUserIdAndProductId(int userId, int productId);
    List<Review> findByProduct(Product product);
}