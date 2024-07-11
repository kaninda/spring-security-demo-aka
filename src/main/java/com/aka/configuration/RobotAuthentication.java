package com.aka.configuration;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RobotAuthentication implements Authentication {

    private Collection<? extends GrantedAuthority> authorities;
    private Object password;
    private Boolean authenticated;

    private RobotAuthentication(Collection<? extends GrantedAuthority> authorities, Object password){
         this.authorities = authorities;
         this.authenticated = !authorities.isEmpty();
         this.password = password;
    }

    //Quand il est authentifié
    public static RobotAuthentication authenticated() {
        return new RobotAuthentication(Collections.singleton(new SimpleGrantedAuthority("ROLE_ROBOT")), null);
    }

    //Quand je ne suis pas authentifié
    public static RobotAuthentication token(String password){
        return new RobotAuthentication(Collections.emptyList(), password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return password;
    }


    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return "Mr Robot principal";
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return "Hello Mr Robot name";
    }
}
