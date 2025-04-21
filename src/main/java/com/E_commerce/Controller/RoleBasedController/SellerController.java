package com.E_commerce.Controller.RoleBasedController;

import com.E_commerce.Entity.Product;
import com.E_commerce.Model.*;
import com.E_commerce.Service.ProductService;
import com.E_commerce.Service.SellerService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import com.E_commerce.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//localhost:9090/e-commerce/products/sellers

@RestController
@PreAuthorize("hasRole('SELLER')")
@RequestMapping("/e-commerce/products/seller")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;


    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getSellerProfile(Authentication authentication) {
        String currentUserEmail = userService.extractEmailFromAuth(authentication);
        UserProfileDTO profile = userService.getUserProfileByEmail(currentUserEmail);
        return ResponseEntity.ok(profile);
    }

    // register
    @PostMapping("/registerSeller")
    public ResponseEntity<String> registerSeller(@Valid @RequestBody SellerRegDTO sellerRegDTO) {
            sellerService.registerSeller(sellerRegDTO);
            return ResponseEntity.ok("Seller registered successfully.");

    }

    //  log in  with username and password
    @PostMapping("/loginSeller")
    public ResponseEntity<String> loginSeller(@Valid @RequestBody SellerLoginDTO sellerLoginDTO) {
            sellerService.loginSeller(sellerLoginDTO.username(), sellerLoginDTO.password());
            return ResponseEntity.ok("Seller logged in successfully.");
    }

    //view their own products
    @GetMapping("/{username}/getSellerProducts")
    public List<Product> getSellerProducts(@PathVariable String username) {
        return sellerService.getSellerProducts(username);
    }

    //  create a new product
    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@Valid @RequestBody SellerProductDTO sellerProductDTO, Authentication authentication) {
        String currentUsername = authentication.getName();
        Product createdProduct = productService.createProduct(sellerProductDTO, currentUsername);
        return ResponseEntity.ok("Your Product is Created... " +createdProduct);
    }

    //  update an existing product
    @PutMapping("/update/{productId}")
    public ResponseEntity<String > updateProduct(@Valid @RequestBody SellerProductDTO sellerProductDTO, Authentication authentication){

        String currentUsername = authentication.getName();
        Product updatedProduct = productService.updateProduct(sellerProductDTO, currentUsername);
        return ResponseEntity.ok("Your Product is Updated... " +updatedProduct);
    }

    //  to delete a product
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@Valid  @RequestBody ProductIdDTO productIdDTO , Authentication authentication  ) throws Exception {
        String currentUsername = authentication.getName();
        productService.deleteProduct(productIdDTO.productId(), currentUsername);
        return ResponseEntity.ok("Product deleted successfully.");
    }

}
