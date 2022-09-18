package com.api.parkingmanagement.resources;

import com.api.parkingmanagement.conifg.security.UserDetailsServiceImpl;
import com.api.parkingmanagement.domain.UserModel;
import com.api.parkingmanagement.domain.requests.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    final UserDetailsServiceImpl userDetailsService;

    public UserController(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<UserModel> saveUser(@RequestBody UserDto user, HttpServletRequest servletRequest) {
        userDetailsService.registerNewUserAccount(user);
        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/" + user.getUserName())
                        .build().toUri()
        ).build();
    }

    @RequestMapping(path = "/{userName}", method = RequestMethod.GET)
    ResponseEntity<Boolean> isUserValid(@PathVariable(value = "userName") String userName) {
        return ResponseEntity.ok().body(
                userDetailsService.loadUserByUsername(userName).isEnabled()
        );
    }
}
