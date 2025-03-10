package com.E_commerce.Service;


import com.E_commerce.Entity.Product;
import com.E_commerce.Entity.Seller;
import com.E_commerce.Repository.ProductRepository;
import com.E_commerce.Repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private ProductRepository productRepository;

    // Register a new seller
    public void registerSeller(Seller seller) throws Exception {
        if (sellerRepository.findByUsername(seller.getUsername()) != null) {
            throw new Exception("Username already taken.");
        }
        sellerRepository.save(seller);
    }

    // Seller login
    public void loginSeller(String username, String password) throws Exception {
        Seller seller = sellerRepository.findByUsername(username);
        if (seller == null || !password.equals(seller.getPassword())) {
            throw new Exception("Invalid login credentials.");
        }
    }

    // Get all products for a seller
    public List<Product> getSellerProducts(String username) {
        Seller seller = sellerRepository.findByUsername(username);
        return productRepository.findBySeller(seller);
    }

}
