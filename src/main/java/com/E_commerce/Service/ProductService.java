package com.E_commerce.Service;

import com.E_commerce.Entity.Product;
import com.E_commerce.Entity.Seller;
import com.E_commerce.Repository.ProductRepository;
import com.E_commerce.Repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*ProductService (Handles product operations)*/

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SellerRepository sellerRepository;

    //used in orderService
    // Fetch product by ID
    public Optional<Product> getProductById(int id) {
        return productRepository.findById(id);
    }

    // Create a product for seller
    public Product createProduct(Product product, String username) throws Exception {
        Seller seller = sellerRepository.findByUsername(username);
        if (seller == null) {
            throw new Exception("Seller not found");
        }
        product.setSeller(seller);
        return productRepository.save(product);
    }

    //get by seller id
    public List<Product> getProductsBySeller(int sellerId) {
        return productRepository.findBySellerId(sellerId);
    }

    // Update a product for seller
    public Product updateProduct(int productId, Product productDetails, String username) throws Exception {
        Product product = productRepository.findById(productId).orElseThrow(() -> new Exception("Product not found"));
        if (!product.getSeller().getUsername().equals(username)) {
            throw new Exception("Unauthorized to update this product");
        }
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStock(productDetails.getStock());

        return productRepository.save(product);
    }

    //delete by id with username   for seller
    public void deleteProduct(int productId, String username) throws Exception {
        Product product = productRepository.findById(productId).orElseThrow(() -> new Exception("Product not found"));

        if (!product.getSeller().getUsername().equals(username)) {
            throw new Exception("Unauthorized to delete this product");
        }

        productRepository.delete(product);
    }

    //delete by id
    public void deleteProductID(int id) {
        productRepository.deleteById(id);
    }

    // products with pagination and filtering
    public Page<Product> getAllProducts(int page, int size, Long minPrice, Long maxPrice, String nameFilter, String categoryFilter) {
        Pageable pageable = PageRequest.of(page, size);


        if (minPrice != null && maxPrice != null) {
            return productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        } else if (nameFilter != null && !nameFilter.isEmpty()) {
            return productRepository.findByNameContaining(nameFilter, pageable);
        } else if (categoryFilter != null && !categoryFilter.isEmpty()) {
            return productRepository.findByCategory(categoryFilter, pageable);
        }

        return productRepository.findAll(pageable);
    }

}
