package com.E_commerce.Repository;

import com.E_commerce.Entity.Cart;
import com.E_commerce.Entity.Product;
import com.E_commerce.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
        void deleteByUser(User user);
    List<Cart> findByUser(User user);
    Optional<Cart> findByUserAndProduct(User user, Product product);
    void deleteByUserAndProduct(User user, Product product);
}
