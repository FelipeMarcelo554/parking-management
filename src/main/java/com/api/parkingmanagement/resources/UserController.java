package com.api.parkingmanagement.resources;

import com.api.parkingmanagement.config.security.UserDetailsServiceImpl;
import com.api.parkingmanagement.domain.UserModel;
import com.api.parkingmanagement.domain.requests.RoleRequest;
import com.api.parkingmanagement.domain.requests.RoleToUserRequest;
import com.api.parkingmanagement.domain.requests.UserDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    final UserDetailsServiceImpl userService;

    public UserController(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<UserModel> saveUser(@RequestBody UserDto user) {

        userService.saveUser(user);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentContextPath().path("/" + user.getUserName()).build().toUri()).build();
    }

    @RequestMapping(path = "checkUser/{userName}", method = RequestMethod.GET)
    ResponseEntity<Boolean> isUserValid(@PathVariable(value = "userName") String userName) {
        return ResponseEntity.ok().body(userService.isUserValid(userName));
    }

    @RequestMapping(path = "/role", method = RequestMethod.POST)
    ResponseEntity<RoleRequest> saveRole(@RequestBody @Valid RoleRequest roleRequest) {

        userService.saveRole(roleRequest);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/role").build().toUri()).body(roleRequest);
    }

    @RequestMapping(path = "/addRoleToUser", method = RequestMethod.POST)
    ResponseEntity<?> addRoleToUser(@RequestBody @Valid RoleToUserRequest roleToUserRequest) {

        userService.addRoleToUser(roleToUserRequest);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "/token/refresh", method = RequestMethod.GET)
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String userName = decodedJWT.getSubject();
                UserDetails user = userService.loadUserByUsername(userName);
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> tokens = new HashMap<>();
                tokens.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }
        } else {
            throw new RuntimeException("Regresh token is not valid!");
        }
    }
}
