package com.aka.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class RobotFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

         if(! Collections.list(request.getHeaderNames()).contains("x-robot-password")){
           filterChain.doFilter(request, response);
           return;
        }
        String robotPassword = request.getHeader("x-robot-password");
        if( robotPassword != null && robotPassword.equals("bipbip")){
            SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
            emptyContext.setAuthentication(new RobotAuthentication());
            SecurityContextHolder.setContext(emptyContext);
            filterChain.doFilter(request, response);
        }else {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().println("You're not a robot, and you are not allowed");
            return;
        }
    }
}
