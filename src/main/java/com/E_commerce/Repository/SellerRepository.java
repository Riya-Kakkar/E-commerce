package com.E_commerce.Repository;

import com.E_commerce.Entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Integer> {
    Seller findByUsername(String username);
}