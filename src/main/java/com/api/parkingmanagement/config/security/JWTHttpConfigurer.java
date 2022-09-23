package com.api.parkingmanagement.config.security;

import com.api.parkingmanagement.config.filter.CustomAuthenticationFilter;
import com.api.parkingmanagement.config.filter.CustonAuthorizationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class JWTHttpConfigurer extends AbstractHttpConfigurer<JWTHttpConfigurer, HttpSecurity> {

    @Override
    public void configure(HttpSecurity http){
        final AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilter(new CustomAuthenticationFilter(authenticationManager));
        http.addFilterBefore(new CustonAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
