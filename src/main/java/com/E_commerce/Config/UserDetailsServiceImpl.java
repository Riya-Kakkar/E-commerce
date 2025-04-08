package com.E_commerce.Config;

import com.E_commerce.Entity.User;
import com.E_commerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new CustomUserDetails(user);
    }

  /*  @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byUsername = userRepository.findByUsername(username); // Find user by username from the DB
        if (byUsername.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        User user = byUsername.get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        System.out.println("Loaded user: " + user.username() + " with role: " + user.role() + " having authorities: " + customUserDetails.getAuthorities());
        return new CustomUserDetails(user);
    }*/
}
