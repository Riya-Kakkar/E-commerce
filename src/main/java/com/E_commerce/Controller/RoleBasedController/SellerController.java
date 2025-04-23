package com.E_commerce.Controller.RoleBasedController;

import com.E_commerce.Entity.Product;
import com.E_commerce.Entity.User;
import com.E_commerce.Helper.SellerAlreadyExistsException;
import com.E_commerce.Model.*;
import com.E_commerce.Service.ProductService;
import com.E_commerce.Service.SellerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import com.E_commerce.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
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


    @PostMapping("/loginSeller")
    public ResponseEntity<String> loginSeller(@Valid @RequestBody  AuthRequest authRequest, @RequestHeader("Authorization") String bearerToken) {

        String token = bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : bearerToken;
        String result = sellerService.loginSeller(authRequest, token);
        return ResponseEntity.ok(result);
    }

    //view their own products
    @GetMapping("/getSellerProducts")
    public List<Product> getSellerProducts( Authentication authentication ) {
        String currentUsername = authentication.getName();
        return sellerService.getSellerProducts(currentUsername);
    }

    //  create a new product
    @PostMapping("/create")
    public ResponseEntity<SellerProductResponseDTO> createProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Long price,
            @RequestParam("stock") Integer stock,
            @RequestParam("category") String category,
            @RequestParam("image") MultipartFile image,
            Authentication authentication
    ) throws  IOException {

        String imageUrl = productService.saveImage(image);

        // Create DTO record
        SellerProductDTO sellerProductDTO = new SellerProductDTO(
                0, name, description, price, stock, category, imageUrl
        );

        String currentUsername = authentication.getName();
        Product createdProduct = productService.createProduct(sellerProductDTO, currentUsername);

        SellerProductResponseDTO response = new SellerProductResponseDTO(
                createdProduct.getId(), "Product created successfully.");

        return ResponseEntity.ok(response);

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
