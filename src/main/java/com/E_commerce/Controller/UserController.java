package com.E_commerce.Controller;

import com.E_commerce.Config.CustomUserDetails;
import com.E_commerce.Entity.User;
import com.E_commerce.Model.AuthRequest;
import com.E_commerce.Model.AuthResponse;
import com.E_commerce.Model.UserDTO;
import com.E_commerce.JWTSecurity.JwtTokenUtil;
import com.E_commerce.Model.UserProfileDTO;
import com.E_commerce.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
            userService.registerUser(userDto);
            return ResponseEntity.ok("User registered successfully!");
    }

    //handling user login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest) {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
//            String jwt = jwtTokenUtil.generateToken(userDetails.getUsername());      //user1
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwt = jwtTokenUtil.generateToken(userDetails.getUser().getEmail());

        return ResponseEntity.ok(new AuthResponse(jwt));

    }

    //logout
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out successfully");
    }

    //get user profile
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getProfile(Authentication authentication) {
        String currentUsername = authentication.getName();
        User user = userService.getUserByUsername(currentUsername);
        UserProfileDTO userProfileDTO = new UserProfileDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isEnable()
        );
        return ResponseEntity.ok(userProfileDTO);
    }

    // changing user password
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody UserDTO userDTO, Authentication authentication) {
        String currentUsername = authentication.getName();
        String userChangedPassword = userService.changePassword(currentUsername, userDTO.password());
        return ResponseEntity.ok("Password updated successfully for :- " + userChangedPassword);
    }

    //updating user profile
    @PutMapping("/update-profile")
    public ResponseEntity<String> updateProfile(@Valid @RequestBody UserDTO userDTO, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("Authentication failed: no authentication details found.");
        }
        String currentUsername = authentication.getName();
        User updatedUser = new User();
        updatedUser.setUsername(userDTO.username());
        updatedUser.setEmail(userDTO.email());
        userService.updateProfile(currentUsername, updatedUser);
        return ResponseEntity.ok("Profile updated successfully.");
    }
}
