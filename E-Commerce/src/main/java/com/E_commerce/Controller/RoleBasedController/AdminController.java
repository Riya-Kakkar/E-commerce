package com.E_commerce.Controller.RoleBasedController;

import com.E_commerce.Service.AdminDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminDashboardService adminDashboardService;

    //for admin dashboard

  @GetMapping("/dashboard")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> getAdminDashboard() {
      return ResponseEntity.ok("Welcome Admin!");
  }

}
