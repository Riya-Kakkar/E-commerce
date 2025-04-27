package com.E_commerce.Service;

import com.E_commerce.Entity.*;
import com.E_commerce.Helper.CartNotFoundException;
import com.E_commerce.Helper.OrderNotFoundException;
import com.E_commerce.Helper.UnauthorizedAccessException;
import com.E_commerce.Model.OrderUpdateStatus;
import com.E_commerce.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    //place an order
    public Order placeOrder( Authentication authentication ) {
        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));



        List<Cart> cartItems = cartRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new CartNotFoundException("Cart is empty");
        }

        long calculatedTotalAmount = 0;

        Order order = new Order(user, calculatedTotalAmount, OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);

        for (Cart cartItem : cartItems) {
            long price = cartItem.getProduct().getPrice();
            OrderItem orderItem = new OrderItem(cartItem.getProduct(), savedOrder, cartItem.getQuantity(), price);
            orderItemRepository.save(orderItem);

            calculatedTotalAmount += price * cartItem.getQuantity();
        }

        savedOrder.setTotalAmount(calculatedTotalAmount);
        orderRepository.save(savedOrder);

        cartRepository.deleteAll(cartItems);
        return savedOrder;
    }

    public List<Order> getUserOrders(  Authentication authentication ) {

        String username = authentication.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return orderRepository.findByUser(user);
    }

    // Update order status
    public void updateOrderStatus(OrderUpdateStatus orderUpdateStatus  , Authentication authentication) {
        Order order = orderRepository.findById(orderUpdateStatus.orderId()).orElseThrow(() -> new OrderNotFoundException("Order not found"));


       //admin
        String username = authentication.getName();
        User user = order.getUser();
        if (!user.getEmail().equals(username) && !authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
            throw new UnauthorizedAccessException("You are not authorized to update the status of this order.");
        }

        String newStatus = orderUpdateStatus.status().toUpperCase();

        try {
            OrderStatus statusEnum = OrderStatus.valueOf(newStatus);

            if (statusEnum != OrderStatus.PENDING && statusEnum != OrderStatus.SHIPPED && statusEnum != OrderStatus.DELIVERED) {
                throw new IllegalArgumentException("Invalid order status: " + newStatus);
            }

            order.setStatus(statusEnum);
            orderRepository.save(order);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status: " + newStatus);
        }
    }

//use in  review service
    public boolean checkIfUserHasPurchasedProduct(User user, Product product) {

        List<Order> orders = orderRepository.findByUser(user);

        for (Order order : orders) {
            for (OrderItem item : order.getOrderItems()) {
                if (item.getProduct().getId() == product.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

}