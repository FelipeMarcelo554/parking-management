package com.api.parkingmanagement.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    private static final String[] PUBLIC_MATCHERS = {
            "/api/user/token/refresh",
            "/api/user/checkUser/**"
    };

    private static final String[] ALL_ROLES = {
            "ROLE_USER",
            "ROLE_MANAGER",
            "ROLE_ADMIN",
            "ROLE_SUPER_ADMIN"
    };

    private static final String[] MANAGER_ROLES = {
            "ROLE_MANAGER",
            "ROLE_ADMIN",
            "ROLE_SUPER_ADMIN"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/login").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, PUBLIC_MATCHERS).permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/user").hasAnyRole(MANAGER_ROLES);
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/parking-spot/**").hasAnyRole(ALL_ROLES);
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/parking-spot/**").hasRole("ROLE_SUPER_ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        http.apply(new JWTHttpConfigurer());
        return http.build();
    }
}
