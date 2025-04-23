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
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellerRepository sellerRepository;

    public String saveImage(MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty.");
        }

        File saveDir = new ClassPathResource("static/images").getFile();
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path imagePath = Paths.get(saveDir.getAbsolutePath() + File.separator + fileName);
        Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        return "/images/" + fileName;
    }

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
        product.setImageUrl(sellerProductDTO.image());
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

        Long priceMin = productFilterDTO.priceMin() != null ? productFilterDTO.priceMin() : 0L;
        Long priceMax = productFilterDTO.priceMax() != null ? productFilterDTO.priceMax() : Long.MAX_VALUE;

        Page<Product> products;

        if (productFilterDTO.category() != null && !productFilterDTO.category().isEmpty()) {
            products = productRepository.findByCategoryAndPriceBetween(productFilterDTO.category(), priceMin, priceMax, pageable);
        } else if (productFilterDTO.name() != null && !productFilterDTO.name().isEmpty()) {
            products = productRepository.findByNameContainingIgnoreCaseAndPriceBetween(productFilterDTO.name(), priceMin, priceMax, pageable);
        } else if (productFilterDTO.stock() != null && productFilterDTO.stock() >= 0) {
            products = productRepository.findByStock(productFilterDTO.stock(), pageable);
        } else {
            products = productRepository.findByPriceBetween(priceMin, priceMax, pageable);
        }
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found matching the given filters.");
        }

        return products;

    }

}
