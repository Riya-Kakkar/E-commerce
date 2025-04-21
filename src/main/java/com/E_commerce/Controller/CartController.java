package com.E_commerce.Controller;


import com.E_commerce.Entity.Cart;
import com.E_commerce.Entity.Order;
import com.E_commerce.Entity.Product;
import com.E_commerce.Entity.User;
import com.E_commerce.Service.CartService;
import com.E_commerce.Service.ProductService;
import com.E_commerce.Service.UserService;
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


    //for cart

    // Add product to cart
    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestParam int userId, @RequestParam int productId, @RequestParam int quantity) {
        User user = userService.getUserById(userId).orElse(null);
        Product product = productService.getProductById(productId);
        if (user != null && product != null) {
            Cart cart = cartService.addToCart(user, product, quantity);

            System.out.println("Adding product to cart: User ID: " + userId + ", Product ID: " + productId + ", Quantity: " + quantity);
            return ResponseEntity.ok(cart);
        }
        return ResponseEntity.badRequest().body(null);
    }

    // Get user's cart
    @GetMapping("/getUserCart/{userId}")
    public ResponseEntity<List<Cart>> getUserCart(@PathVariable int userId) {
        System.out.println("Fetching cart for user with ID: " + userId);
        User user = userService.getUserById(userId).orElse(null);
        if (user != null) {
            return ResponseEntity.ok(cartService.getUserCart(user));
        }
        return ResponseEntity.notFound().build();
    }



    // Remove from cart
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestParam int userId, @RequestParam int productId ) {
        User user = userService.getUserById(userId).orElse(null);
        Product product = productService.getProductById(productId);
        if (user != null && product != null) {
            System.out.println("Removing product from cart: User ID: " + userId + ", Product ID: " + productId);
            cartService.removeFromCart(user, product);
            return ResponseEntity.ok("Product removed from cart.");
        }
        return ResponseEntity.badRequest().body("User or Product not found.");
    }


    // Clear cart
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable int userId ) {
        User user = userService.getUserById(userId).orElse(null);
        if (user != null) {
            System.out.println("Clearing cart for user with ID: " + userId);
            cartService.clearCart(user);
            return ResponseEntity.ok("Cart cleared successfully.");
        }
        return ResponseEntity.notFound().build();
    }

}
