package com.E_commerce.Controller;


import com.E_commerce.Entity.Product;
import com.E_commerce.Model.ProductFilterDTO;
import com.E_commerce.Model.ProductRespDTO;
import com.E_commerce.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//localhost:9090/e-commerce/products

@RestController
@RequestMapping("/e-commerce/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    //firstly you need to register a seller  , then login it ---so go to seller controller
    @GetMapping("/getProduct/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable int productId ) {
            Product product = productService.getProductById(productId);
            return ResponseEntity.ok(product);
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<ProductRespDTO> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Long price,
            @RequestParam(required = false) Integer stock,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        ProductFilterDTO filterDTO = new ProductFilterDTO(name, category, price, stock, page, size);
        Page<Product> products = productService.getAllProducts(filterDTO);
        return ResponseEntity.ok(new ProductRespDTO("Note: As a customer, you can only view products", products));
    }

}
