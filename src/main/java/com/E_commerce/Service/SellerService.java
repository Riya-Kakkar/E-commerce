package com.E_commerce.Service;


import com.E_commerce.Entity.Product;
import com.E_commerce.Entity.User;
import com.E_commerce.Helper.ProductNotFoundException;
import com.E_commerce.Helper.UnauthorizedAccessException;
import com.E_commerce.Model.ProductFilterDTO;
import com.E_commerce.Model.ProductRequestDTO;
import com.E_commerce.Model.SellerProductDTO;
import com.E_commerce.Repository.ProductRepository;
import com.E_commerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class SellerService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    public Product createProduct(ProductRequestDTO productRequestDTO, MultipartFile file, String username , String path) throws IOException {
        User seller =  userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!seller.getRole().equals("ROLE_SELLER")) {
            throw new UnauthorizedAccessException("You are not authorized to create a product. Please check your permissions or login with a seller account.");
        }

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Product image file must be provided.");
        }

         //file name
        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IllegalArgumentException("Invalid image file name.");
        }

        String randomID = UUID.randomUUID().toString();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = randomID + fileExtension;


        //full path
        String imagePath = path + File.separator + fileName;
        //create folder if not created
        File dir = new File(imagePath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        // file copy
        Files.copy(file.getInputStream(), Paths.get(imagePath), StandardCopyOption.REPLACE_EXISTING);

        Product product = new Product();
        product.setName(productRequestDTO.name());
        product.setDescription(productRequestDTO.description());
        product.setPrice(productRequestDTO.price());
        product.setStock(productRequestDTO.stock());
        product.setCategory(productRequestDTO.category());
        product.setSeller(seller);
         product.setImageUrl("/images/" + fileName);
        return productRepository.save(product);
    }

    // Update a product for seller
    public Product updateProduct( SellerProductDTO sellerProductDTO, String username){

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
    public void deleteProduct(int productId, String username)  {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (!product.getSeller().getUsername().equals(username)) {
            throw new UnauthorizedAccessException("Unauthorized to delete this product");
        }

        productRepository.delete(product);
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
