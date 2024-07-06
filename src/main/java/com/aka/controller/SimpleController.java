package com.aka.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class SimpleController {

    @GetMapping("/private")
    public String privatePage(Authentication authentication) {

        return "hello 🥳🤩" + getUSerName(authentication) + " this is a private part";
    }


    @PreAuthorize("hasRole('ROLE_ROBOT')")
    @GetMapping("/robot")
    public String robotPage(Authentication authentication){
        System.out.println(authentication.getName());
        return "Hello Robot";
    }

    @GetMapping("/")
    public String publicPage() {
        return "hello, this is only public side";
    }



    private String getUSerName(Authentication authentication) {
        return Optional.of(authentication)
                .filter(OAuth2AuthenticationToken.class::isInstance)
                .map(OAuth2AuthenticationToken.class::cast)
                .map(OAuth2AuthenticationToken::getPrincipal)
                .map(DefaultOidcUser.class::cast)
                .map(DefaultOidcUser::getEmail)
                .orElse(authentication.getName());
    }
}
