package com.E_commerce.Controller.RoleBasedController;

import com.E_commerce.Entity.Product;
import com.E_commerce.Model.*;
import com.E_commerce.Service.SellerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import com.E_commerce.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

//localhost:9090/e-commerce/products/sellers

@RestController
@RequestMapping("/e-commerce/seller")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private UserService userService;

    @Value("${project.image}")
    private String path;

    //  create a new product
    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@RequestParam("product") String productData,
                                           @RequestParam("image") MultipartFile image,
                                           Authentication authentication) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ProductRequestDTO productRequestDTO = mapper.readValue(productData, ProductRequestDTO.class);
        String currentUsername = authentication.getName();
        Product savedProduct = sellerService.createProduct(productRequestDTO, image, currentUsername ,path);
        return ResponseEntity.ok("Product is added. ID :- " + savedProduct.getId());
    }

    //  update an existing product
    @PutMapping("/update")
    public ResponseEntity<String > updateProduct(@Valid @RequestBody SellerProductDTO sellerProductDTO, Authentication authentication){

        String currentUsername = authentication.getName();
        Product updatedProduct = sellerService.updateProduct(sellerProductDTO, currentUsername);
        UpdatedProductResponse response = new UpdatedProductResponse(updatedProduct);
        return ResponseEntity.ok(response.toString());
    }

    //  to delete a product
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable int productId, Authentication authentication  )  {
        String currentUsername = authentication.getName();
        sellerService.deleteProduct(productId, currentUsername);
        return ResponseEntity.ok("Product deleted successfully.");
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
