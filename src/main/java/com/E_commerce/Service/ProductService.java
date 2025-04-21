package com.E_commerce.Service;

import com.E_commerce.Entity.Product;
import com.E_commerce.Entity.Seller;
import com.E_commerce.Helper.ProductNotFoundException;
import com.E_commerce.Helper.SellerNotFoundException;
import com.E_commerce.Helper.UnauthorizedAccessException;
import com.E_commerce.Model.ProductFilterDTO;
import com.E_commerce.Model.SellerProductDTO;
import com.E_commerce.Repository.ProductRepository;
import com.E_commerce.Repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellerRepository sellerRepository;

    // Create a product for seller
    public Product createProduct(SellerProductDTO sellerProductDTO, String username) {
        Seller seller = sellerRepository.findByUsername(username);
        if (seller == null) {
            throw new SellerNotFoundException("Seller not found with username :- " +username);
        }
        Product product = new Product();
        product.setName(sellerProductDTO.name());
        product.setDescription(sellerProductDTO.description());
        product.setPrice(sellerProductDTO.price());
        product.setStock(sellerProductDTO.stock());
        product.setCategory(sellerProductDTO.category());
        product.setSeller(seller);

        return productRepository.save(product);
    }

    // Update a product for seller
    public Product updateProduct( SellerProductDTO sellerProductDTO, String username)   {
        Product product = productRepository.findById(sellerProductDTO.productId()).orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (!product.getSeller().getUsername().equals(username)) {
            throw new UnauthorizedAccessException("You are not allowed to update this product");
        }
        product.setName(sellerProductDTO.name());
        product.setDescription(sellerProductDTO.description());
        product.setPrice(sellerProductDTO.price());
        product.setStock(sellerProductDTO.stock());
        product.setCategory(sellerProductDTO.category());

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


    //used in orderService
    // get product by ID
    public Product getProductById(int productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found with ID :- " + productId));
    }

    // products with pagination and filtering
    public Page<Product> getAllProducts(ProductFilterDTO productFilterDTO) {
        Pageable pageable = PageRequest.of(productFilterDTO.page(), productFilterDTO.size());
        Page<Product> products;

        if (productFilterDTO.price() != null && productFilterDTO.price() > 0) {
            products = productRepository.findByPrice(productFilterDTO.price(), pageable);
        } else if (productFilterDTO.name() != null && !productFilterDTO.name().isEmpty()) {
            products = productRepository.findByNameContaining(productFilterDTO.name(), pageable);
        } else if (productFilterDTO.category() != null && !productFilterDTO.category().isEmpty()) {
            products = productRepository.findByCategory(productFilterDTO.category(), pageable);
        } else if (productFilterDTO.stock() != null && productFilterDTO.stock() >= 0) {
            products = productRepository.findByStock(productFilterDTO.stock() , pageable);
        } else {
            products = productRepository.findAll(pageable);
        }

        if (products.isEmpty()) {
            throw new ProductNotFoundException(" No products found matching the given criteria.");
        }

        return products;

    }

}
