package com.api.parkingmanagement.conifg.security;

import com.api.parkingmanagement.domain.UserModel;
import com.api.parkingmanagement.domain.requests.UserDto;
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

    public UserDetailsServiceImpl(UserRepository userRepository, BCryptEncoder bCryptEncoder) {
        this.userRepository = userRepository;
        this.bCryptEncoder = bCryptEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserModel userModel = userRepository.findByUserName(userName).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + userName)
        );
        return new User(userModel.getUsername(), userModel.getPassword(), true, true, true, true, userModel.getAuthorities());
    }

    public UserDto registerNewUserAccount(UserDto user) {
        UserModel userModel = new UserModel();
        userModel.setUserName(user.getUserName());
        userModel.setPassword(bCryptEncoder.passwordEncoder().encode(user.getPassword()));
        userRepository.save(userModel);
        return user;
    }
}
