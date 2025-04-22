package com.E_commerce.Service;

import com.E_commerce.Entity.*;
import com.E_commerce.Helper.CartNotFoundException;
import com.E_commerce.Helper.OrderNotFoundException;
import com.E_commerce.Model.OrderUpdateStatus;
import com.E_commerce.Repository.CartRepository;
import com.E_commerce.Repository.OrderItemRepository;
import com.E_commerce.Repository.OrderRepository;
import com.E_commerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Locale;

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
    public Order placeOrder(User user ) {
        User foundUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Cart> cartItems = cartRepository.findByUser(foundUser);

        if (cartItems.isEmpty()) {
            throw new CartNotFoundException("Cart is empty");
        }

        long calculatedTotalAmount = 0;

        Order order = new Order(foundUser, calculatedTotalAmount, OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);

        for (Cart cartItem : cartItems) {
            long price = cartItem.getProduct().getPrice();
            OrderItem orderItem = new OrderItem(cartItem.getProduct(), savedOrder, cartItem.getQuantity(), price);
            orderItemRepository.save(orderItem);

            calculatedTotalAmount += price * cartItem.getQuantity();
        }

        savedOrder.setTotalAmount(calculatedTotalAmount);
        orderRepository.save(savedOrder);

        cartRepository.deleteByUser(foundUser);

        return savedOrder;
    }

    public List<Order> getUserOrders(User user) {
        user = userRepository.findById(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return orderRepository.findByUser(user);
    }

    // Update order status
    public void updateOrderStatus(OrderUpdateStatus orderUpdateStatus) {
        Order order = orderRepository.findById(orderUpdateStatus.orderId()).orElseThrow(() -> new OrderNotFoundException("Order not found"));

        String newStatus = orderUpdateStatus.status().toUpperCase();

        //  Validate against allowed status values
        List<String> validStatuses = List.of(
                OrderStatus.PENDING,
                OrderStatus.SHIPPED,
                OrderStatus.DELIVERED
        );

        if (!validStatuses.contains(newStatus)) {
            throw new IllegalArgumentException("Invalid order status: " + newStatus);
        }

        order.setStatus(newStatus);
        orderRepository.save(order);
    }


//optional
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