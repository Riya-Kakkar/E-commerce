package com.E_commerce.Config;

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

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class MyConfig {

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Public Endpoints
                        .requestMatchers("/e-commerce/user/register", "/e-commerce/user/login").permitAll()
                        .requestMatchers("/e-commerce/reviews/product/**", "/e-commerce/reviews/average/**").permitAll()

                        // Authenticated Endpoints
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
                .httpBasic(withDefaults()) // API-based authentication (no UI)
                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }


   /* @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
           http
                   .authorizeHttpRequests(auth->auth
                          *//* .requestMatchers("/e-commerce/user/register").permitAll()
                           .requestMatchers("/e-commerce/user/login").permitAll()*//*
                          *//* .requestMatchers("/e-commerce/user/register", "/e-commerce/user/login").authenticated() *//*
//                           .requestMatchers("/e-commerce/user/register", "/user/login").permitAll()
//                           .requestMatchers("/e-commerce/user/**").authenticated()
                          *//* .requestMatchers("/e-commerce/user/update-profile").permitAll()*//*
                           .requestMatchers("/e-commerce/user/register", "/e-commerce/user/login").permitAll()
                           .requestMatchers("/e-commerce/orders/**").authenticated() // Require login for orders
                           .requestMatchers("/e-commerce/reviews/add").authenticated()
                           .requestMatchers("/e-commerce/reviews/markInappropriate/**", "/e-commerce/reviews/delete/**").hasRole("ADMIN")
                           .requestMatchers("/e-commerce/reviews/product/**", "/reviews/average/**").permitAll()
                           .requestMatchers("/e-commerce/admin-dashboard/**").authenticated()
                           .requestMatchers("/e-commerce/admin/**").hasRole("ADMIN")
                           .requestMatchers("/e-commerce/customer/**").hasRole("CUSTOMER")
                           .requestMatchers("/e-commerce/seller/**").hasRole("SELLER")
                           *//*.requestMatchers("/e-commerce/**").permitAll()*//*
                           .requestMatchers("/e-commerce/**").authenticated()
                           .anyRequest().authenticated()
                   )
                   .httpBasic(withDefaults())  //dis is for api based authentication --------- no view page
                   .csrf(csrf -> csrf.disable()
                   )
                   .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        return http.build();
    }
*/
}
