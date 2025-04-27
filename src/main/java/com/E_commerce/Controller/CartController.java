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
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<String> addToCart(@Valid @RequestBody CartAddDTO cartAddDTO , Authentication authentication) {
        String username = authentication.getName();
        cartService.addProductToCart(cartAddDTO ,username);
            return ResponseEntity.ok("Product added to cart!");
    }


    // Remove from cart
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(@Valid @RequestBody CartRemoveDTO cartRemoveDTO , Authentication authentication) {
        String username = authentication.getName();
        cartService.removeProductFromCart(cartRemoveDTO , username);
        return ResponseEntity.ok("Product removed from cart");

    }

    // Get user's cart
    @GetMapping("/getUserCart")
    public ResponseEntity<List<Cart>> getUserCart(  Authentication authentication) {
        String username = authentication.getName();
        List<Cart> cartItems = cartService.getUserCartItems(username);
        return ResponseEntity.ok(cartItems);
    }

    // Clear cart
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart( Authentication authentication) {
        String username = authentication.getName();
        cartService.clearCart(username);
            return ResponseEntity.ok("Cart cleared successfully.");
    }

}
