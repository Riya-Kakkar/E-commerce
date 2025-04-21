package com.E_commerce.Service;


import com.E_commerce.Entity.Product;
import com.E_commerce.Entity.Seller;
import com.E_commerce.Helper.InvalidCredentialsException;
import com.E_commerce.Helper.SellerAlreadyExistsException;
import com.E_commerce.Helper.SellerNotFoundException;
import com.E_commerce.Model.SellerRegDTO;
import com.E_commerce.Repository.ProductRepository;
import com.E_commerce.Repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private ProductRepository productRepository;

    public void registerSeller(SellerRegDTO sellerRegDTO)  {
        if (sellerRepository.findByUsername(sellerRegDTO.username()) != null) {
            throw new SellerAlreadyExistsException("Username already taken.");
        }

        Seller seller = new Seller();
        seller.setUsername(sellerRegDTO.username());
        seller.setEmail(sellerRegDTO.email());
        seller.setPassword(sellerRegDTO.password());

        sellerRepository.save(seller);
    }

    // Seller login
    public void loginSeller(String username, String password)  {
        Seller seller = sellerRepository.findByUsername(username);
        if (seller == null) {
            throw new SellerNotFoundException("Seller not found with username: " + username);
        }
        if ( !password.equals(seller.getPassword())) {
                throw new InvalidCredentialsException("Invalid password!");
            }
    }

    // Get all products for a seller
    public List<Product> getSellerProducts(String username) {
        Seller seller = sellerRepository.findByUsername(username);
        return productRepository.findBySeller(seller);
    }

}
