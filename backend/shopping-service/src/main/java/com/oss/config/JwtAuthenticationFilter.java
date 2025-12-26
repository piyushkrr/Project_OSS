package com.oss.config;

import com.oss.service.CustomUserDetailsService;
import com.oss.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        System.out.println("Processing request: " + request.getRequestURI() + ", AuthHeader: "
                + (authHeader != null ? "Present" : "Missing"));
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwt = authHeader.substring(7);
            userEmail = jwtUtil.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                System.out.println("Processing authentication for email: " + userEmail);
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                System.out.println("User loaded: " + userDetails.getUsername());

                boolean isValid = jwtUtil.isTokenValid(jwt, userDetails);
                System.out.println("Token valid: " + isValid);

                if (isValid) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Authentication successful set in SecurityContext");
                } else {
                    System.out.println("Token validation failed for user: " + userEmail);
                }
            }
        } catch (Exception e) {
            System.err.println("Authentication error in Filter: " + e.getMessage());
            e.printStackTrace();
            SecurityContextHolder.clearContext();

            // Return 500 Internal Server Error so we see the message and don't trigger 403
            // handler
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Authentication failed: " + e.getMessage() + "\"}");
            return; // Stop filter chain
        }

        filterChain.doFilter(request, response);
    }
}
