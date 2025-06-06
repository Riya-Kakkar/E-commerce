package com.E_commerce.Service;

import com.E_commerce.Entity.Cart;
import com.E_commerce.Entity.Product;
import com.E_commerce.Entity.User;
import com.E_commerce.Helper.ProductNotFoundException;
import com.E_commerce.Model.CartAddDTO;
import com.E_commerce.Repository.CartRepository;
import com.E_commerce.Repository.ProductRepository;
import com.E_commerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

 // add to cart
    public Cart addProductToCart(CartAddDTO cartAddDTO, String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Product product = productRepository.findById(cartAddDTO.productId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        Optional<Cart> existingCart = cartRepository.findByUserAndProduct(user, product);

        Cart cart;
        if (existingCart.isPresent()) {
             cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + cartAddDTO.quantity());
            return cartRepository.save(cart);
        } else {
             cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setQuantity(cartAddDTO.quantity());

        }
        return cartRepository.save(cart);
    }

    // Remove from cart
    public void removeProductFromCart(int productId , String username) {

        Cart cart = cartRepository.findByProductIdAndUserEmail(productId, username);
        if (cart != null) {
            cartRepository.delete(cart);
        }
    }

    // Get user cart
    public List<Cart> getUserCartItems(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return cartRepository.findByUser(user);
    }

    // Clear user's cart
    public void clearCart(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Cart> userCart = cartRepository.findByUser(user);
        cartRepository.deleteAll(userCart);
    }

}
