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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(Authentication authentication){
        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        userRepository.deleteById(user.getId());

        return ResponseEntity.ok("User deleted");
    }

    @PutMapping("/user/{id}/role")
    public ResponseEntity<?> updateUserRole(
            @PathVariable int UserId,
           @Valid @RequestBody UpdateRoleRequest updateRoleRequest) {

            User updatedUser = adminService.updateUserRole(UserId, updateRoleRequest.role());
            return ResponseEntity.ok("User role updated to " + updatedUser.getRole());
    }


    @GetMapping("/stats")
    public ResponseEntity<AdminDashboardDTO> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }
}
