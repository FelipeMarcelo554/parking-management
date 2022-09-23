package com.api.parkingmanagement.config.security;

import com.api.parkingmanagement.domain.RoleModel;
import com.api.parkingmanagement.domain.UserModel;
import com.api.parkingmanagement.domain.requests.RoleRequest;
import com.api.parkingmanagement.domain.requests.RoleToUserRequest;
import com.api.parkingmanagement.domain.requests.UserDto;
import com.api.parkingmanagement.repository.RoleRepository;
import com.api.parkingmanagement.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    final UserRepository userRepository;
    final BCryptEncoder bCryptEncoder;
    final RoleRepository roleRepository;

    public UserDetailsServiceImpl(UserRepository userRepository, BCryptEncoder bCryptEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.bCryptEncoder = bCryptEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) {
        UserModel userModel = userRepository.findByUserName(userName).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + userName)
        );
        return new User(userModel.getUsername(), userModel.getPassword(), true, true, true, true, userModel.getAuthorities());
    }

    public UserModel saveUser(UserDto user) {

        UserModel userModel = new UserModel();
        userModel.setUserName(user.getUserName());
        userModel.setPassword(bCryptEncoder.passwordEncoder().encode(user.getPassword()));
        return userRepository.save(userModel);
    }

    public Boolean isUserValid(String userName) {
        return loadUserByUsername(userName).isEnabled();
    }

    public RoleModel saveRole(RoleRequest roleRequest) {

        RoleModel roleModel = new RoleModel();
        roleModel.setRoleName(roleRequest.getRoleName());
        return roleRepository.save(roleModel);
    }

    public UserModel addRoleToUser(RoleToUserRequest roleToUserRequest) {

        UserModel userModel = userRepository.findByUserName(roleToUserRequest.getUserName()).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + roleToUserRequest.getUserName())
        );

        RoleModel roleModel = roleRepository.findByRoleName(roleToUserRequest.getRoleName()).orElseThrow(
                () -> new IllegalArgumentException("Role with name of " + roleToUserRequest.getRoleName())
        );

        userModel.getRoles().add(roleModel);
        return userRepository.save(userModel);
    }
}
