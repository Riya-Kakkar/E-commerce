package com.E_commerce.Controller.RoleBasedController;

import com.E_commerce.Entity.Product;
import com.E_commerce.Entity.Seller;
import com.E_commerce.Service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/sellers")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @GetMapping("/get-products")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<String> getSellerProducts() {
        return ResponseEntity.ok("Welcome Seller!");
    }


    // to register an account
    @PostMapping("/registerSeller")
    public ResponseEntity<String> registerSeller(@RequestBody Seller seller) {
        try {
            sellerService.registerSeller(seller);
            return ResponseEntity.ok("Seller registered successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    //  log in  with username and password
    @PostMapping("/loginSeller")
    public ResponseEntity<String> loginSeller(@RequestParam String username, @RequestParam String password) {
        try {
            sellerService.loginSeller(username, password);
            return ResponseEntity.ok("Seller logged in successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    //view their own products
    @GetMapping("/{username}/getSellerProducts")
    public List<Product> getSellerProducts(@PathVariable String username) {
        return sellerService.getSellerProducts(username);
    }
}
