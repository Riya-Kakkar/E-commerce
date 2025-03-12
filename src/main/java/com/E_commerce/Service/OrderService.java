package com.E_commerce.Service;

import com.E_commerce.Entity.*;
import com.E_commerce.Repository.CartRepository;
import com.E_commerce.Repository.OrderItemRepository;
import com.E_commerce.Repository.OrderRepository;
import com.E_commerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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


    //place an order
    public Order placeOrder(int userId , long totalAmount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Cart> cartItems = cartRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        long calculatedTotalAmount = 0L;
        Order order = new Order(user, totalAmount, "Pending");

        Order savedOrder = orderRepository.save(order);

        for (Cart cartItem : cartItems) {
            long price = cartItem.getProduct().getPrice();
            OrderItem orderItem = new OrderItem(cartItem.getProduct(), savedOrder, cartItem.getQuantity(), price);
            orderItemRepository.save(orderItem);

            totalAmount += price * cartItem.getQuantity();
        }

        savedOrder.setTotalAmount(totalAmount);
        orderRepository.save(savedOrder);

        cartRepository.deleteByUser(user);

        return savedOrder;
    }

    // Get user orders

    public List<Order> getUserOrders(User user) {
         user = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUser(user);
    }

    // Update order status
    public Order updateOrderStatus(int orderId, String status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            return orderRepository.save(order);
        }
        return null;
    }

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