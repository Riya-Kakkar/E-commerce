package com.E_commerce.Service;


import com.E_commerce.Config.JwtAuthenticationFilter;
import com.E_commerce.Config.JwtTokenUtil;
import com.E_commerce.Entity.Product;
import com.E_commerce.Entity.Seller;
import com.E_commerce.Helper.InvalidCredentialsException;
import com.E_commerce.Helper.SellerNotFoundException;
import com.E_commerce.Model.AuthRequest;
import com.E_commerce.Repository.ProductRepository;
import com.E_commerce.Repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private  UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    // Seller login
    public String loginSeller(AuthRequest authRequest , String token)  {

        UserDetails userDetailsFromToken = jwtTokenUtil.extractUserDetailsFromToken(token);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        UserDetails userDetailsFromAuth = (UserDetails) authentication.getPrincipal();

        if (!userDetailsFromToken.getUsername().equals(userDetailsFromAuth.getUsername())) {
            throw new AccessDeniedException("Token does not match authenticated user.");
        }

        boolean hasSellerRole = userDetailsFromToken.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_SELLER"));

        if (!hasSellerRole) {
            throw new AccessDeniedException("Access denied. Seller role required.");
        }

        return "Seller authenticated successfully with existing token.";
    }

    // Get all products for a seller
    public List<Product> getSellerProducts(String username) {
        Seller seller = sellerRepository.findByUsername(username);
        return productRepository.findBySeller(seller);
    }

}
