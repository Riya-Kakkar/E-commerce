package com.E_commerce.Controller;

import com.E_commerce.Entity.Cart;
import com.E_commerce.Entity.Product;
import com.E_commerce.Entity.User;
import com.E_commerce.Model.CartAddDTO;
import com.E_commerce.Model.CartRemoveDTO;
import com.E_commerce.Service.CartService;
import com.E_commerce.Service.ProductService;
import com.E_commerce.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//localhost:9090/e-commerce/cart

@RestController
@RequestMapping("/e-commerce/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;


    // Add product to cart
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@Valid @RequestBody CartAddDTO cartAddDTO) {
              cartService.addToCart(cartAddDTO);
            return ResponseEntity.ok("Product added to cart!");
    }


    // Remove from cart
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(@Valid @RequestBody CartRemoveDTO cartRemoveDTO ) {
         cartService.removeFromCart(cartRemoveDTO);
        return ResponseEntity.ok("Product removed from cart");

    }

    // Get user's cart
    @GetMapping("/getUserCart")
    public ResponseEntity<List<Cart>> getUserCart( @RequestParam User userId) {
        List<Cart> cartItems = cartService.getUserCart(userId);
        return ResponseEntity.ok(cartItems);
    }

    // Clear cart
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable User userId ) {
            cartService.clearCart(userId);
            return ResponseEntity.ok("Cart cleared successfully.");
    }

}
