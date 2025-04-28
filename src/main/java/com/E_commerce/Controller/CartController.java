package com.E_commerce.Controller;

import com.E_commerce.Entity.Cart;
import com.E_commerce.Model.CartAddDTO;
import com.E_commerce.Service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//localhost:9090/e-commerce/cart

@RestController
@RequestMapping("/e-commerce/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // Add product to cart
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@Valid @RequestBody CartAddDTO cartAddDTO , Authentication authentication) {
        String username = authentication.getName();
        cartService.addProductToCart(cartAddDTO ,username);
            return ResponseEntity.ok("Product added to cart!");
    }

    // Get user's cart
    @GetMapping("/getUserCart")
    public ResponseEntity<List<Cart>> getUserCart(  Authentication authentication) {
        String username = authentication.getName();
        List<Cart> cartItems = cartService.getUserCartItems(username);
        return ResponseEntity.ok(cartItems);
    }

    // Remove from cart
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeFromCart(@PathVariable int productId , Authentication authentication) {
        String username = authentication.getName();
        cartService.removeProductFromCart(productId , username);
        return ResponseEntity.ok("Product removed from cart");

    }


    // Clear cart
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart( Authentication authentication) {
        String username = authentication.getName();
        cartService.clearCart(username);
            return ResponseEntity.ok("Cart cleared successfully.");
    }

}
