package com.E_commerce.Controller;

import com.E_commerce.Model.*;
import com.E_commerce.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

//localhost:9090/e-commerce/user

@RestController
@RequestMapping("/e-commerce/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDTO userDto) {
            userService.registerUser(userDto);
            return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest) {
        String token = userService.loginUser(authRequest);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getProfile(Authentication authentication) {

        String currentUserEmail = authentication.getName();

//        String currentUserEmail = userService.extractEmailFromAuth(authentication);
        UserProfileDTO profile = userService.getUserProfileByEmail(currentUserEmail);

        return ResponseEntity.ok(profile);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody PasswordChangeDTO passwordChangeDTO, Authentication authentication) {
        String currentUserEmail = userService.extractEmailFromAuth(authentication);
        String userChangedPassword = userService.changePasswordByEmail(currentUserEmail, passwordChangeDTO.password());
        return ResponseEntity.ok("Password updated successfully for Email: " + userChangedPassword);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<String> updateProfile(@Valid @RequestBody UserDTO userDTO, Authentication authentication) {
        String currentUserEmail = userService.extractEmailFromAuth(authentication);
        userService.updateUserProfileByEmail(currentUserEmail, userDTO);
        return ResponseEntity.ok("Profile updated successfully.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out successfully");
    }

}
