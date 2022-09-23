package com.api.parkingmanagement.config.security;

import com.api.parkingmanagement.config.filter.CustomAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    private static final String[] PUBLIC_MATCHERS={
      "/api/user/token/refresh"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/login").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, PUBLIC_MATCHERS).permitAll();
//        http.authorizeRequests().antMatchers(PUBLIC_MATCHERS).permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.apply(new JWTHttpConfigurer());

//                .authorizeHttpRequests()
//                .antMatchers(HttpMethod.GET, "/parking-spot/**").hasAnyRole("ADMIN", "USER")
//                .antMatchers(HttpMethod.POST, "/parking-spot").hasAnyRole("ADMIN", "USER")
//                .antMatchers(HttpMethod.DELETE, "/parking-spot/**").hasRole("ADMIN")


        return http.build();
    }
}
