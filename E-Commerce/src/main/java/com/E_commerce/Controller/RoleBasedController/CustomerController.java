package com.E_commerce.Controller.RoleBasedController;

import com.E_commerce.Entity.Product;
import com.E_commerce.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class CustomerController {

    @Autowired
    private ProductService productService;

    @GetMapping("/customers/profile")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> getCustomerProfile() {
        return ResponseEntity.ok("Welcome Customer!");
    }
}
