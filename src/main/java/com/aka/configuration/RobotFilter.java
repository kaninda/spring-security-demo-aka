package com.aka.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class RobotFilter extends OncePerRequestFilter {

    private final String HEADER_NAME = "x-robot-password";
    private final AuthenticationManager authenticationManager;

    public RobotFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

         if(! Collections.list(request.getHeaderNames()).contains("x-robot-password")){
           filterChain.doFilter(request, response);
           return;
        }

        String robotPassword = request.getHeader(HEADER_NAME);
        RobotAuthentication token = RobotAuthentication.token(robotPassword);
        //Il va appeler l authenticate de authenticationProvider
        Authentication authentication = authenticationManager.authenticate(token);
        if(authentication != null){
            SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
            emptyContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(emptyContext);
            filterChain.doFilter(request, response);
        }else {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().println("You're not a robot, and you are not allowed");
            return;
        }
    }
}
