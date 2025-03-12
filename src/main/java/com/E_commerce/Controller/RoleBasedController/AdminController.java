package com.E_commerce.Controller.RoleBasedController;

import com.E_commerce.Service.AdminDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

//localhost:9090/e-commerce/admin

@RestController
@RequestMapping("/e-commerce/admin")
public class AdminController {

    @Autowired
    private AdminDashboardService adminDashboardService;

    //for admin dashboard

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
