package com.aka.configuration;

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

        RobotConfigurer robotCongigurer = new RobotConfigurer().password("beep-boop").password("bipbip");
        http.apply(robotCongigurer);
        return http.authorizeHttpRequests(
                        auth -> {
                            auth.requestMatchers("/private").authenticated();
                            auth.anyRequest().permitAll();
                        }
                ).formLogin(Customizer.withDefaults())
                .authenticationProvider(new DanielAuthenticationProvider())
                .oauth2Login(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
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
