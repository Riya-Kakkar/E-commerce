package com.E_commerce.Controller.RoleBasedController;

import com.E_commerce.Model.AdminDashboardDTO;
import com.E_commerce.Model.UserProfileDTO;
import com.E_commerce.Service.AdminService;
import com.E_commerce.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//localhost:9090/e-commerce/admin

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/e-commerce/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    //for admin dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<UserProfileDTO> getAdminDashboard(Authentication authentication) {
        String currentUserEmail = userService.extractEmailFromAuth(authentication);
        UserProfileDTO profile = userService.getUserProfileByEmail(currentUserEmail);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/stats")
    public ResponseEntity<AdminDashboardDTO> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }
}
