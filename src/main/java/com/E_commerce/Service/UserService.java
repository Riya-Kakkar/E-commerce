package com.E_commerce.Service;

import com.E_commerce.Config.CustomUserDetails;
import com.E_commerce.Entity.User;
import com.E_commerce.Helper.UnauthorizedAccessException;
import com.E_commerce.Config.JwtTokenUtil;
import com.E_commerce.Model.UserDTO;
import com.E_commerce.Model.UserProfileDTO;
import com.E_commerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final List<String> allowedRoles = List.of("ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_SELLER");

    //user registering
    public void registerUser(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.username()).isPresent()) {
            throw new IllegalArgumentException("Username '" + userDTO.username() + "' already exists.");
        }

        if (userRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new IllegalArgumentException("Email '" + userDTO.email() + "' already exists.");
        }

        String formattedRole = userDTO.role().toUpperCase().startsWith("ROLE_") ? userDTO.role().toUpperCase() : "ROLE_" + userDTO.role().toUpperCase();

        if (!allowedRoles.contains(formattedRole)) {
            throw new IllegalArgumentException("Invalid role! Allowed roles: ADMIN, CUSTOMER, SELLER.");
        }

        User user = new User(0, userDTO.username(), userDTO.email(),
                passwordEncoder.encode(userDTO.password()), formattedRole, true);

        userRepository.save(user);
    }

    //user by email --- used in loginUser
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    // user login
    public String loginUser(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = getUserByEmail(email);
        String token = jwtTokenUtil.generateToken(user.getEmail());
        System.out.println("Token created for user: " + user.getEmail());

        return token;
    }

   // used in userController-- getProfile , changePassword  , updateProfile
    public String extractEmailFromAuth(Authentication authentication) {
        if (authentication == null) {
            throw new UnauthorizedAccessException("User is not authenticated");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getEmail();
    }

    //used in order,cart,review
    public Optional<User> getUserById(int userId) {
        return userRepository.findById(userId);
    }

    //get user profile by email
    public UserProfileDTO getUserProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        UserProfileDTO userProfileData = new UserProfileDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isEnable());
        return userProfileData;

    }

    // changing user password by email
    public String changePasswordByEmail(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        String encryptedPassword = passwordEncoder.encode(newPassword);
        user = new User(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                encryptedPassword,
                user.getRole(),
                user.isEnable()
        );
        userRepository.save(user);
        return user.getEmail();
    }

    //updating user profile by email
    public void updateUserProfileByEmail(String email, UserDTO userDTO) {
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        currentUser.setUsername(userDTO.username());
        currentUser.setPassword(passwordEncoder.encode(userDTO.password()));
        currentUser.setRole(userDTO.role().startsWith("ROLE_") ? userDTO.role() : "ROLE_" + userDTO.role());
        currentUser.setEnable(true);
        userRepository.save(currentUser);
    }

}