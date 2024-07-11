package com.aka.configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Arrays;
import java.util.List;

public class RobotAuthenticationProvider implements AuthenticationProvider {

    private final List<String> password;

    public RobotAuthenticationProvider(String ...password) {
        this.password = Arrays.asList(password);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var auth = (RobotAuthentication)authentication;
        if (password.contains(auth.getCredentials())){
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
