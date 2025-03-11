package com.E_commerce.Controller.RoleBasedController;

import com.E_commerce.Entity.Product;
import com.E_commerce.Entity.Seller;
import com.E_commerce.Service.ProductService;
import com.E_commerce.Service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/e-commerce/products/sellers")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private ProductService productService;

    @GetMapping("/sellerDashboard")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<String> sellerDashboard(Principal principal) {
        System.out.println("Welcome Seller!" +principal.getName());
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

    //  create a new product
    @PostMapping("/createProduct")
    public ResponseEntity<String> createProduct(@RequestBody Product product, @RequestParam String username) throws Exception {
        System.out.println("Adding new product with details: ");
        Product createdProduct = productService.createProduct(product, username);
        System.out.println("Your Product is Created..." + createdProduct);
        return ResponseEntity.ok("Your Product is Created... " +createdProduct);
    }

    //  update an existing product
    @PutMapping("/updateProduct/{productId}")
    public ResponseEntity<String > updateProduct(
            @PathVariable int productId,
            @RequestBody Product productDetails,
            @RequestParam String username) throws Exception {

        Product updatedProduct = productService.updateProduct(productId, productDetails, username);
        System.out.println("Your Product is Updated... ");
        return ResponseEntity.ok("Your Product is Updated... " +updatedProduct);
    }

    //  to delete a product
    @DeleteMapping("/deleteProduct/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable int productId, @RequestParam String username) throws Exception {
        productService.deleteProduct(productId, username);
        System.out.println("Your Product is Deleted...");
        return ResponseEntity.ok("Product deleted successfully.");
    }

}
