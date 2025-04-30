package com.E_commerce.Service;

import com.E_commerce.Entity.User;
import com.E_commerce.Model.AdminDashboardDTO;
import com.E_commerce.Repository.OrderRepository;
import com.E_commerce.Repository.ProductRepository;
import com.E_commerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

    private final List<String> allowedRoles = List.of("ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_SELLER");

    public AdminDashboardDTO getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalProducts = productRepository.count();
        long totalOrders = orderRepository.count();
        double totalRevenue = orderRepository.calculateTotalRevenue();

        return new AdminDashboardDTO(totalUsers, totalProducts, totalOrders, totalRevenue);
    }

    public User updateUserRole(int userId, String role) {

        if (!allowedRoles.contains(role.toUpperCase())) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setRole(role.toUpperCase());
        return userRepository.save(user);
    }
}
