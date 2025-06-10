package com.wcs.employee_management.employee_management.security;

import com.wcs.employee_management.employee_management.entity.User;
import com.wcs.employee_management.employee_management.exception.InvalidUserException;
import com.wcs.employee_management.employee_management.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final List<String> PUBLIC_APIS = List.of("/auth/login", "/user/create");

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public JWTAuthenticationFilter(JWTUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/api/login");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(TOKEN_PREFIX.length());

        try {
            String email = jwtUtil.extractEmail(token);

            if (email != null && jwtUtil.isTokenValid(token)) {
                User user = userRepository.findByEmail(email);
                if (user == null) {
                    throw new InvalidUserException("User not found");
                }

                // Convert user's role to Spring Security authority
                String roleName = "ROLE_" + user.getRole().getCode();
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        email, null, List.of(authority));
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: " + ex.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }
}
