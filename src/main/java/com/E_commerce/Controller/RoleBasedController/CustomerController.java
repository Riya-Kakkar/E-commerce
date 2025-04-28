package com.E_commerce.Controller.RoleBasedController;

import com.E_commerce.Entity.Product;
import com.E_commerce.Model.ProductFilterDTO;
import com.E_commerce.Model.ProductRespDTO;
import com.E_commerce.Model.UserProfileDTO;
import com.E_commerce.Service.ProductService;
import com.E_commerce.Service.SellerService;
import com.E_commerce.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


//localhost:9090/e-commerce/products/customers

@RestController
@PreAuthorize("hasRole('CUSTOMER')")
@RequestMapping("/e-commerce/customer")
public class CustomerController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getCustomerProfile(Authentication authentication) {
        String username = authentication.getName();
        UserProfileDTO profile = userService.getUserProfileByEmail(username);
        return ResponseEntity.ok(profile);
    }


    @GetMapping("/getAllProducts")
    public ResponseEntity<ProductRespDTO> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Long priceMin,
            @RequestParam(required = false) Long priceMax,
            @RequestParam(required = false) Integer stock,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        ProductFilterDTO filterDTO = new ProductFilterDTO(name, category,  priceMin, priceMax, stock, page, size);
        ProductRespDTO products = sellerService.getAllProducts(filterDTO);
        return ResponseEntity.ok(products);
    }
}
