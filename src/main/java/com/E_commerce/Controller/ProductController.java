package com.E_commerce.Controller;


import com.E_commerce.Entity.Product;
import com.E_commerce.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//localhost:9090/e-commerce/products

@RestController
@RequestMapping("/e-commerce/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    //firstly you need to register a seller ---so go to seller controller

    @GetMapping("/getProduct/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable int productId ) {
            Product product = productService.getProductById(productId);
            return ResponseEntity.ok(product);
    }

}
