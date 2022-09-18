package com.api.parkingmanagement.conifg.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    final BCryptEncoder bCryptEncoder;

    public WebSecurityConfig(BCryptEncoder bCryptEncoder) {
        this.bCryptEncoder = bCryptEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic().and()
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.GET, "/parking-spot/**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/parking-spot").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.DELETE, "/parking-spot/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and().csrf().and().cors().disable();

        return http.build();
    }
}
