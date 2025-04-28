package com.E_commerce.Helper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String requestURI = request.getRequestURI();
        String message = "Access denied: You do not have permission.";

        if (requestURI.contains("/e-commerce/reviews/delete")) {
            message = "Only admins can delete reviews.";
        } else if (requestURI.contains("/e-commerce/reviews/markInappropriate")) {
            message = "Only admins can mark reviews as inappropriate.";
        } else if (requestURI.contains("/e-commerce/orders/update-status")) {
            message = "Only admins can update order status.";
        }

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }

}
