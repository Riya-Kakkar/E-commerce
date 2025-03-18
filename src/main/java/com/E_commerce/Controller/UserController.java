package com.E_commerce.Controller;

import com.E_commerce.Entity.User;
import com.E_commerce.Model.UserRequest;
import com.E_commerce.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

//localhost:9090/e-commerce/user

@RestController
@RequestMapping("/e-commerce/user")
public class UserController {

    @Autowired
    private UserService userService;

    //user registering
    @PostMapping("/register")
    public ResponseEntity<String> registerUser( @Valid @RequestBody UserRequest userRequest) {
        return userService.registerUser(userRequest);
    }


    //handling user login
    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
    public ResponseEntity<String> login( @Valid @RequestBody UserRequest userRequest) {
        try {
              Optional<User> authenticatedUser = userService.authenticateUser(userRequest.getEmail(), userRequest.getPassword());
            if (authenticatedUser.isPresent()) {
                System.out.println("     LOGIN USER -      " + authenticatedUser.get().username());
                return ResponseEntity.ok("Logged in as: " + authenticatedUser.get().username());
            }
        } catch (Exception e) {
            System.out.println("Authentication failed for username/email : " + userRequest.getEmail());
            return ResponseEntity.status(401).body("   Authentication failed for username/email :   " +userRequest.getEmail());
        }
        return ResponseEntity.status(401).body(" Authentication failed: Invalid username or password ");
    }

    //logout
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        System.out.println(" User Logged out successfully");
        return ResponseEntity.ok("Logged out successfully");
    }

    //get user profile
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Authentication authentication) {
        String currentUsername = authentication.getName();
        User user = userService.getUserByUsername(currentUsername);
        return ResponseEntity.ok(user);
    }


    // changing user password
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword( @Valid @RequestBody UserRequest userRequest, Authentication authentication) {
        String currentUsername = authentication.getName();
        String userChanged = userService.changePassword(currentUsername, userRequest.getPassword());
        return ResponseEntity.ok("Password updated successfully for :- " +userChanged);
    }

    //updating user profile
    @PutMapping("/update-profile")
    public ResponseEntity<String> updateProfile(@Valid @RequestBody UserRequest userRequest ,Authentication authentication) {

        System.out.println("current user authenticated of authentication-- " + authentication);
        if (authentication == null) {
            return ResponseEntity.status(401).body("Authentication failed: no authentication details found.");
        }

        String currentUsername = authentication.getName();
        System.out.println("current user authenticated-- " + currentUsername);

        User currentUser = userService.getUserByUsername(currentUsername);
        System.out.println("current user -- " + currentUser);


        if (currentUser.role().equals("ROLE_admin") || currentUser.username().equals(userRequest.getUsername())) {

            if (userRequest.getUsername() != null && !userRequest.getUsername().equals(currentUser.username())) {
                if (userService.isUsernameTaken(userRequest  .getUsername())) {
                    return ResponseEntity.status(400).body("Username already taken.");
                }
            }



            // Create a new User instance with the updated details (since User is immutable)
            User updatedUser = new User(
                    currentUser.id(),
                    userRequest.getUsername() != null ? userRequest.getUsername() : currentUser.username(),
                    userRequest.getEmail() != null ? userRequest.getEmail() : currentUser.email(),
                    currentUser.password(),  // Keep the existing password
                    currentUser.role(),      // Keep the existing role
                    currentUser.enable()     // Keep the existing active status
            );

            try {
                userService.updateProfile(currentUsername, updatedUser);  // saves the user entity to DB
                System.out.println("Profile updated successfully.");
                return ResponseEntity.ok("Profile updated successfully.");
            } catch (Exception e) {
                // Handle any errors during profile update (e.g., database issues)
                System.out.println("Error during profile update: " + e.getMessage());
                return ResponseEntity.status(500).body("An error occurred while updating the profile.");
            }

        } else {
            System.out.println("You are not authorized to update this profile.");
            return ResponseEntity.status(403).body("You are not authorized to update this profile.");
        }
    }

}
