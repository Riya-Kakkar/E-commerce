package com.E_commerce.Controller;

import com.E_commerce.Entity.Order;
import com.E_commerce.Entity.User;
import com.E_commerce.Service.OrderService;
import com.E_commerce.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
//localhost:9090/e-commerce/orders

@RestController
@RequestMapping("/e-commerce/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    // Place an order
    @PostMapping("/place/{userId}")
    public ResponseEntity<Order> placeOrder(@PathVariable int userId, @RequestParam long totalAmount) {
        System.out.println("Placing order for user with ID: " + userId + " and total amount: " + totalAmount);
        Optional<User> userOptional = userService.getUserById(userId);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Order order = orderService.placeOrder(userId, totalAmount);
        return ResponseEntity.ok(order);

    }


    /*// Place an order
    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@RequestParam int userId, @RequestParam long totalAmount) {
        System.out.println("Placing order for user with ID: " + userId + " and total amount: " + totalAmount);
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            Order order = orderService.placeOrder(userOptional.get(), totalAmount);
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }*/


    // Get user orders
    @GetMapping("/getUserOrders/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable int userId) {
        System.out.println("Fetching orders for user with ID: " + userId);
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            List<Order> orders = orderService.getUserOrders(userOptional.get());
            return ResponseEntity.ok(orders);
        }
        return ResponseEntity.notFound().build();
    }

    // Update order status
    @PutMapping("/{orderId}/update-status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable int orderId, @RequestParam String status) {
        orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok("Order status updated");
    }

}
