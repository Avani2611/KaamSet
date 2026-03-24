package com.kaamSet.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    
    @Override
    
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {



        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        String role = jwtService.extractRole(token);


        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(
                email,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );


            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println("JWT EMAIL: " + email);
            System.out.println("JWT ROLE: ROLE_" + role);
            System.out.println("AUTH OBJECT: " + SecurityContextHolder.getContext().getAuthentication());
        
        }
        
        } catch (Exception e) {
             e.printStackTrace(); //THIS will show real issue in terminal
        }



        filterChain.doFilter(request, response);
    }

    
}
