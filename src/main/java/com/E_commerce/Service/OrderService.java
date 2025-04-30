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

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    //place an order
    public Order placeOrder( Authentication authentication ) {
        String username = authentication.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Cart> cartItems = cartRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new CartNotFoundException(" Your Cart is empty");
        }

        long calculatedTotalAmount = 0;

        Order order = new Order(user, calculatedTotalAmount, OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();

        for (Cart cartItem : cartItems) {
            long price = cartItem.getProduct().getPrice();
            OrderItem orderItem = new OrderItem(cartItem.getProduct(), order, cartItem.getQuantity(), price);

            orderItems.add(orderItem);
            calculatedTotalAmount += price * cartItem.getQuantity();
        }

        order.setTotalAmount(calculatedTotalAmount);
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        orderItemRepository.saveAll(orderItems);

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
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new UnauthorizedAccessException("Only admins can update order status.");
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