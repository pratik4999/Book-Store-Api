package com.bookstore.filters;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis(); // Track execution time

        // Get request details
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String ipAddress = request.getRemoteAddr(); // Client IP Address

        // Get authentication details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null) ? authentication.getName() : "Anonymous";
        Collection<? extends GrantedAuthority> authorities = (authentication != null) ? authentication.getAuthorities() : null;

        // Log basic request info
        System.out.println("🔹 Request: " + method + " " + requestURI);
        System.out.println("🔹 IP Address: " + ipAddress);
        System.out.println("🔹 User: " + username);
        System.out.println("🔹 Roles: " + (authorities != null ? authorities : "None"));

        // Log request headers
        System.out.println("🔹 Headers: ");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println("   ↳ " + headerName + ": " + request.getHeader(headerName));
        }

        // Continue request processing
        filterChain.doFilter(request, response);

        // Calculate execution time
        long executionTime = System.currentTimeMillis() - startTime;
        System.out.println("✅ Request completed in " + executionTime + "ms\n");
    }
}
