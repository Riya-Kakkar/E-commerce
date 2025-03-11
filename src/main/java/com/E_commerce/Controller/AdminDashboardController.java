package com.E_commerce.Controller;

import com.E_commerce.Service.AdminDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

//localhost:9090/e-commerce/admin-dashboard

@RestController
@RequestMapping("/e-commerce/admin-dashboard")
public class AdminDashboardController {

    @Autowired
    private AdminDashboardService adminDashboardService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getAdminDashboard(Principal principal) {
        System.out.println("Welcome Admin!" +principal.getName());
        return ResponseEntity.ok("Welcome Admin!" +principal.getName());
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = adminDashboardService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

}
