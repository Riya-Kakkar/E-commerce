package com.E_commerce.Config;

import com.E_commerce.JWTSecurity.JwtAuthenticationFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class MyConfig {

    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    @Bean
    public UserDetailsService getUserDetailsService() {
        return new UserDetailsServiceImpl();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Public Endpoints
                        .requestMatchers("/e-commerce/user/register", "/e-commerce/user/login").permitAll()
                        .requestMatchers("/e-commerce/reviews/product/**", "/e-commerce/reviews/average/**").permitAll()

                        // Authenticated Endpoints
                        .requestMatchers("/e-commerce/user/**").authenticated()
                        .requestMatchers("/e-commerce/orders/**").authenticated()
                        .requestMatchers("/e-commerce/reviews/add").authenticated()
                        .requestMatchers("/e-commerce/admin-dashboard/**").authenticated()

                        // Role-based Access Control
                        .requestMatchers("/e-commerce/reviews/markInappropriate/**", "/e-commerce/reviews/delete/**").hasRole("ADMIN")
                        .requestMatchers("/e-commerce/admin/**").hasRole("ADMIN")
                        .requestMatchers("/e-commerce/customer/**").hasRole("CUSTOMER")
                        .requestMatchers("/e-commerce/seller/**").hasRole("SELLER")

                        // Secure all other endpoints
                        .requestMatchers("/e-commerce/**").authenticated()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
