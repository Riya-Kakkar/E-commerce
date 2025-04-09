package com.E_commerce.Controller.RoleBasedController;

import com.E_commerce.Entity.Product;
import com.E_commerce.Model.UserProfileDTO;
import com.E_commerce.Service.ProductService;
import com.E_commerce.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


//localhost:9090/e-commerce/products/customers

@RestController
@RequestMapping("/e-commerce/products/customer")
public class CustomerController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getCustomerProfile(Authentication authentication) {
        String currentUserEmail = userService.extractEmailFromAuth(authentication);
        UserProfileDTO profile = userService.getUserProfileByEmail(currentUserEmail);
        return ResponseEntity.ok(profile);
    }


    @GetMapping("/getAllProducts")
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


}
