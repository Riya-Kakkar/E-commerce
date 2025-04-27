package com.E_commerce.Controller;

import com.E_commerce.Entity.Order;
import com.E_commerce.Model.OrderUpdateStatus;
import com.E_commerce.Service.OrderService;
import com.E_commerce.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//localhost:9090/e-commerce/orders

@RestController
@RequestMapping("/e-commerce/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    // Place an order
    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(Authentication authentication) {
        Order order = orderService.placeOrder(authentication);
        return ResponseEntity.ok(order);
    }

    // Get user orders
    @GetMapping("/getUserOrders")
    public ResponseEntity<List<Order>> getUserOrders(Authentication authentication) {
        List<Order> orders = orderService.getUserOrders(authentication);
        return ResponseEntity.ok(orders);
    }

    // Update order status
    @PutMapping("/update-status")
    public ResponseEntity<String> updateOrderStatus(@Valid @RequestBody OrderUpdateStatus orderUpdateStatus, Authentication authentication) {
        orderService.updateOrderStatus(orderUpdateStatus, authentication);
        return ResponseEntity.ok("Order status updated");

    }

}