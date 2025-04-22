package com.E_commerce.Controller;

import com.E_commerce.Entity.Order;
import com.E_commerce.Entity.User;
import com.E_commerce.Model.OrderUpdateStatus;
import com.E_commerce.Service.OrderService;
import com.E_commerce.Service.UserService;
import jakarta.validation.Valid;
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
    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@RequestParam User userId) {
        Order order = orderService.placeOrder(userId);
        return ResponseEntity.ok(order);
    }

    // Get user orders
    @GetMapping("/getUserOrders/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable User userId) {
            List<Order> orders = orderService.getUserOrders(userId);
            return ResponseEntity.ok(orders);
    }

    // Update order status
    @PutMapping("/update-status")
    public ResponseEntity<String> updateOrderStatus(@Valid  @RequestBody OrderUpdateStatus orderUpdateStatus) {
        orderService.updateOrderStatus(orderUpdateStatus);
        return ResponseEntity.ok("Order status updated");
    }

}
