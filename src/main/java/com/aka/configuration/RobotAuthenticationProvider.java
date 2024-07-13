package com.aka.configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Arrays;
import java.util.List;

public class RobotAuthenticationProvider implements AuthenticationProvider {

    private final List<String> passwords;

    public RobotAuthenticationProvider(List <String> passwords) {
        this.passwords = passwords;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var auth = (RobotAuthentication)authentication;
        if (passwords.contains(auth.getCredentials())){
            return RobotAuthentication.authenticated();
        }else{
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RobotAuthentication.class.isAssignableFrom(authentication);
    }
}
