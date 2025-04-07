package com.E_commerce.Controller;

import com.E_commerce.Entity.User;
import com.E_commerce.Helper.AuthenticationException;
import com.E_commerce.Helper.UserNotFoundException;
import com.E_commerce.Model.AuthRequest;
import com.E_commerce.Model.AuthResponse;
import com.E_commerce.Model.UserDTO;
import com.E_commerce.JWTSecurity.JwtTokenUtil;
import com.E_commerce.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


//localhost:9090/e-commerce/user

@RestController
@RequestMapping("/e-commerce/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authManager;

    //user registering
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDto) {
        try {
            userService.registerUser(userDto);
            return ResponseEntity.ok("User registered successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error registering the user.");
        }
    }

    // Get user by ID
    @GetMapping("/getUser/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        return ResponseEntity.ok(user);
    }


    //handling user login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtTokenUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(new AuthResponse(jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    //logout
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
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
    public ResponseEntity<String> changePassword(@Valid @RequestBody UserDTO userRequest, Authentication authentication) {
        String currentUsername = authentication.getName();
        String userChangedPassword = userService.changePassword(currentUsername, userRequest.password());
        return ResponseEntity.ok("Password updated successfully for :- " + userChangedPassword);
    }

    //updating user profile
    @PutMapping("/update-profile")
    public ResponseEntity<String> updateProfile(@Valid @RequestBody UserDTO userRequest, Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity.status(401).body("Authentication failed: no authentication details found.");
        }

        String currentUsername = authentication.getName();
        User currentUser = userService.getUserByUsername(currentUsername);
        User updatedUser = new User(
                currentUser.getId(),
                userRequest.password() != null ? userRequest.username() : currentUser.getUsername(),
                userRequest.email() != null ? userRequest.email() : currentUser.getEmail(),
                currentUser.getPassword(),
                currentUser.getRole(),
                currentUser.isEnable()
        );
        return ResponseEntity.ok("Profile updated successfully.");
    }
}
