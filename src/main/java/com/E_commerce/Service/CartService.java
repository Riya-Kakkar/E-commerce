package com.E_commerce.Service;

import com.E_commerce.Entity.Cart;
import com.E_commerce.Entity.Product;
import com.E_commerce.Entity.User;
import com.E_commerce.Repository.CartRepository;
import com.E_commerce.Repository.ProductRepository;
import com.E_commerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    public CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

//get cart items
    public List<Cart> getCartItems(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartRepository.findByUser(user);
    }

 // add to cart
    public Cart addToCart(User user, Product product, int quantity) {
         user = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
         product = productRepository.findById(product.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<Cart> existingCart = cartRepository.findByUserAndProduct(user, product);

        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + quantity);
            return cartRepository.save(cart);
        } else {
            Cart newCart = new Cart(user, product, quantity);
            return cartRepository.save(newCart);
        }
    }

    // Remove from cart
    public void removeFromCart(User user, Product product) {
        cartRepository.deleteByUserAndProduct(user, product);
    }


    // Get user cart
    public List<Cart> getUserCart(User user) {
        return cartRepository.findByUser(user);
    }

    // Clear user's cart
    public void clearCart(User user) {
        List<Cart> userCart = cartRepository.findByUser(user);
        cartRepository.deleteAll(userCart);
    }

}
