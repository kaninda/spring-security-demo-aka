package com.aka.configuration;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationEventPublisher eventPublisher) throws Exception {
        http.getSharedObject(AuthenticationManagerBuilder.class).authenticationEventPublisher(eventPublisher);
       //Solution temporaire pour recuperer l' authenticationManager
        //1. Creation du Provider manager, mais c'est springboot qui devrait nous le retourner
        var manager = new ProviderManager(new RobotAuthenticationProvider("beep-boop", "bipbip"));
        manager.setAuthenticationEventPublisher(eventPublisher);
        return http.authorizeHttpRequests(
                        auth -> {
                            auth.requestMatchers("/private").authenticated();
                            auth.anyRequest().permitAll();
                        }
                ).formLogin(Customizer.withDefaults())
                .authenticationProvider(new DanielAuthenticationProvider())
                .oauth2Login(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(
                        new RobotFilter(manager),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(new User("arnaud", "{noop}kaninda", Collections.EMPTY_LIST));
    }

    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> onSuccess(){
        return event -> {
            var authClassName = event.getAuthentication().getClass().getSimpleName();
            String userName = event.getAuthentication().getName();
            System.out.println(" 🍾 🍾  Login successful "
            + authClassName+ " - "
            + userName);
        };
    }

}
