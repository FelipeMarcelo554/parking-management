package com.api.parkingmanagement;

import com.api.parkingmanagement.config.security.UserDetailsServiceImpl;
import com.api.parkingmanagement.domain.requests.RoleRequest;
import com.api.parkingmanagement.domain.requests.RoleToUserRequest;
import com.api.parkingmanagement.domain.requests.UserDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.transaction.Transactional;

@SpringBootApplication
public class ParkingManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingManagementApplication.class, args);
	}

	@Bean
	@Transactional
	CommandLineRunner run(UserDetailsServiceImpl userService){

		return args -> {

			userService.saveRole(new RoleRequest("ROLE_USER"));
			userService.saveRole(new RoleRequest("ROLE_MANAGER"));
			userService.saveRole(new RoleRequest("ROLE_ADMIN"));
			userService.saveRole(new RoleRequest("ROLE_SUPER_ADMIN"));

			userService.saveUser(new UserDto("john", "senha123"));
			userService.saveUser(new UserDto("will", "senha123"));
			userService.saveUser(new UserDto("jim", "senha123"));
			userService.saveUser(new UserDto("arnold", "senha123"));

			userService.addRoleToUser(new RoleToUserRequest("john", "ROLE_USER"));
			userService.addRoleToUser(new RoleToUserRequest("john", "ROLE_MANAGER"));
			userService.addRoleToUser(new RoleToUserRequest("will", "ROLE_MANAGER"));
			userService.addRoleToUser(new RoleToUserRequest("jim", "ROLE_ADMIN"));
			userService.addRoleToUser(new RoleToUserRequest("arnold", "ROLE_SUPER_ADMIN"));
			userService.addRoleToUser(new RoleToUserRequest("arnold", "ROLE_ADMIN"));
			userService.addRoleToUser(new RoleToUserRequest("arnold", "ROLE_USER"));

		};


	}
}
