package com.E_commerce.Controller;

import com.E_commerce.Entity.User;
import com.E_commerce.Model.UserRequest;
import com.E_commerce.Service.UserService;
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
    /*    public ResponseEntity<User> registerUser(@RequestParam String username, @RequestParam String email, @RequestParam String password, @RequestParam String role) { //we cant used this too bcoz @RequestParam is not binding the request in json format*/
    /*    public ResponseEntity<User> registerUser(@RequestBody String username, @RequestBody String email, @RequestBody String password, @RequestBody String role) {  //causing an error bcoz we cant used multiple @RequestBody annotation*/
    public ResponseEntity<String> registerUser(@RequestBody UserRequest userRequest) {
        System.out.println("----------- Registering User with Details ------------- ");
        System.out.println("USERNAME:- " + userRequest.getUsername());
        System.out.println("EMAIL:- " + userRequest.getEmail());
        System.out.println("PASSWORD:- " + userRequest.getPassword());
        System.out.println("ROLE:- " + userRequest.getRole());
        return userService.registerUser(userRequest);
    }


    //handling user login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        try {
            System.out.println("   LOGIN  for UserName:    " + username);
            Optional<User> authenticatedUser = userService.authenticateUser(username, password);
            if (authenticatedUser.isPresent()) {
                System.out.println("     LOGIN USER -      " + authenticatedUser.get().getUsername());
                return ResponseEntity.ok("Logged in as: " + authenticatedUser.get().getUsername());
            }
        } catch (Exception e) {
            System.out.println("Authentication failed for username: " + username);
            return ResponseEntity.status(401).body("   Authentication failed for username:   " +username);
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
    public ResponseEntity<String> changePassword(@RequestParam String newPassword , Authentication authentication) {
        String currentUsername = authentication.getName();
        String userChanged = userService.changePassword(currentUsername, newPassword);
        return ResponseEntity.ok("Password updated successfully for :- " +userChanged);
    }

    //updating user profile
    @PutMapping("/update-profile")
    public ResponseEntity<String> updateProfile(@RequestBody User user ,Authentication authentication) {
        System.out.println("current user authenticated of authentication-- " + authentication);

        if (authentication == null) {
            return ResponseEntity.status(401).body("Authentication failed: no authentication details found.");
        }
        String currentUsername = authentication.getName();
        System.out.println("current user authenticated-- " + currentUsername);
        User currentUser = userService.getUserByUsername(currentUsername);
        System.out.println("current user -- " + currentUser);

        if (currentUser.getRole().equals("ROLE_admin") || currentUser.getUsername().equals(user.getUsername())) {

            if (user.getUsername() != null && !user.getUsername().equals(currentUser.getUsername())) {
                if (userService.isUsernameTaken(user.getUsername())) {
                    return ResponseEntity.status(400).body("Username already taken.");
                }
            }
            currentUser.setEmail(user.getEmail());

            try {
                userService.updateProfile(currentUsername, user);  // Assuming this method saves the user entity to DB
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
