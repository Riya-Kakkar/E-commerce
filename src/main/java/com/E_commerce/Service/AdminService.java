package com.E_commerce.Service;

import com.E_commerce.Model.AdminDashboardDTO;
import com.E_commerce.Repository.OrderRepository;
import com.E_commerce.Repository.ProductRepository;
import com.E_commerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;


    public AdminDashboardDTO getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalProducts = productRepository.count();
        long totalOrders = orderRepository.count();
        double totalRevenue = orderRepository.calculateTotalRevenue();

        return new AdminDashboardDTO(totalUsers, totalProducts, totalOrders, totalRevenue);
    }
}
