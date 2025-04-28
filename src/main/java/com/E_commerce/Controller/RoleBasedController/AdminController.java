package com.E_commerce.Controller.RoleBasedController;

import com.E_commerce.Entity.User;
import com.E_commerce.Model.AdminDashboardDTO;
import com.E_commerce.Model.UpdateRoleRequest;
import com.E_commerce.Model.UserProfileDTO;
import com.E_commerce.Repository.UserRepository;
import com.E_commerce.Service.AdminService;
import com.E_commerce.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//localhost:9090/e-commerce/admin

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/e-commerce/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository ;


    @GetMapping("/user")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted");
    }

    @PutMapping("/user/{id}/role")
    public ResponseEntity<?> updateUserRole(
            @PathVariable int UserId,
           @Valid @RequestBody UpdateRoleRequest updateRoleRequest) {

            User updatedUser = userService.updateUserRole(UserId, updateRoleRequest.role());
            return ResponseEntity.ok("User role updated to " + updatedUser.getRole());
    }


    //for admin dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<UserProfileDTO> getAdminDashboard(Authentication authentication) {
        String username = authentication.getName();
        UserProfileDTO profile = userService.getUserProfileByEmail(username);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/stats")
    public ResponseEntity<AdminDashboardDTO> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }
}
