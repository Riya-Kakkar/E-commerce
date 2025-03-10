package com.E_commerce.Controller;


import com.E_commerce.Entity.Product;
import com.E_commerce.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // for  product

    @GetMapping("/products/{id}")
    public ResponseEntity<Optional<Product>> getProductById(@PathVariable int id) {
        System.out.println("Get Product by its Id - " +id);
        return ResponseEntity.ok(productService.getProductById(id));
    }

    //with page all product

    @GetMapping("/all")
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category) {

        System.out.println("List of all products are - ");
        Page<Product> products = productService.getAllProducts(page, size, minPrice, maxPrice, name, category);
        return ResponseEntity.ok(products);
    }

   /* @PostMapping("/add_product")
    public ResponseEntity<Product> addProduct(@RequestParam String name, @RequestParam String description,
                                              @RequestParam double price, @RequestParam int stock,
                                              @RequestParam int sellerId) {

        System.out.println("Adding new product with details: ");
        System.out.println("NAME:- " + name);
        System.out.println("DESCRIPTION:- " + description);
        System.out.println("PRICE:- " + price);
        System.out.println("STOCK:- " + stock);
        System.out.println("SELLER_ID:- " + sellerId);
        return ResponseEntity.ok(productService.addProduct(name, description, price, stock, sellerId));
    }*/


    //  create a new product
    @PostMapping("/sellers/createProduct")
    public ResponseEntity<String> createProduct(@RequestBody Product product, @RequestParam String username) throws Exception {
        System.out.println("Adding new product with details: ");
        Product createdProduct = productService.createProduct(product, username);
        System.out.println("Your Product is Created..." + createdProduct);
        return ResponseEntity.ok("Your Product is Created... " +createdProduct);
    }

    //  update an existing product
    @PutMapping("/sellers/updateProduct/{productId}")
    public ResponseEntity<String > updateProduct(
            @PathVariable int productId,
            @RequestBody Product productDetails,
            @RequestParam String username) throws Exception {

        Product updatedProduct = productService.updateProduct(productId, productDetails, username);
        System.out.println("Your Product is Updated... ");
        return ResponseEntity.ok("Your Product is Updated... " +updatedProduct);
    }

    //  to delete a product
    @DeleteMapping("/sellers/deleteProduct/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable int productId, @RequestParam String username) throws Exception {
        productService.deleteProduct(productId, username);
        System.out.println("Your Product is Deleted...");
        return ResponseEntity.ok("Product deleted successfully.");
    }


/*
    @GetMapping("/products/all_product")
    public ResponseEntity<List<Product>> getAllProducts() {
        System.out.println("List of all products are - ");
        return ResponseEntity.ok(productService.getAllProducts());
    }


    @GetMapping("/products/seller/{sellerId}")
    public ResponseEntity<List<Product>> getProductsBySeller(@PathVariable int sellerId) {
        System.out.println("Get products By seller with ID: " + sellerId);
        return ResponseEntity.ok(productService.getProductsBySeller(sellerId));
    }

    @PutMapping("/products/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestParam String name,
                                                 @RequestParam String description, @RequestParam long price,
                                                 @RequestParam int stock, @RequestParam int sellerId) {
        System.out.println("Updating product with ID: " + id);
        System.out.println("NAME: " + name);
        System.out.println("DESCRIPTION: " + description);
        System.out.println("PRICE: " + price);
        System.out.println("STOCK: " + stock);
        System.out.println("SELLER_ID: " + sellerId);
        return ResponseEntity.ok(productService.updateProduct(id, name, description, price, stock, sellerId));
    }
    */
}
