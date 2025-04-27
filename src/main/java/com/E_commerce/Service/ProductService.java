package com.E_commerce.Service;

import com.E_commerce.Entity.Product;
import com.E_commerce.Helper.ProductNotFoundException;
import com.E_commerce.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // get product by ID
    public Product getProductById(int productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found with ID :- " + productId));
    }

}
