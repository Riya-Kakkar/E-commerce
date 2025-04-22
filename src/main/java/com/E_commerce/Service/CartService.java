package com.E_commerce.Service;

import com.E_commerce.Entity.Cart;
import com.E_commerce.Entity.Product;
import com.E_commerce.Entity.User;
import com.E_commerce.Helper.ProductNotFoundException;
import com.E_commerce.Model.CartAddDTO;
import com.E_commerce.Model.CartRemoveDTO;
import com.E_commerce.Repository.CartRepository;
import com.E_commerce.Repository.ProductRepository;
import com.E_commerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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
    public Cart addToCart(CartAddDTO cartAddDTO) {
        User user = userRepository.findById(cartAddDTO.userId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Product product = productRepository.findById(cartAddDTO.productId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        Optional<Cart> existingCart = cartRepository.findByUserAndProduct(user, product);

        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + cartAddDTO.quantity());
            return cartRepository.save(cart);
        } else {
            Cart newCart = new Cart(user, product, cartAddDTO.quantity());
            return cartRepository.save(newCart);
        }
    }

    // Remove from cart
    public void removeFromCart(CartRemoveDTO cartRemoveDTO) {
        User foundUser = userRepository.findById(cartRemoveDTO.userId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Product foundProduct = productRepository.findById(cartRemoveDTO.productId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

         cartRepository.deleteByUserAndProduct(foundUser, foundProduct);

    }

    // Get user cart
    public List<Cart> getUserCart(User user) {
        return cartRepository.findByUser(user);
    }


    // Clear user's cart
    public void clearCart(User user) {
        User foundUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Cart> userCart = cartRepository.findByUser(foundUser);
        cartRepository.deleteAll(userCart);
    }

}
