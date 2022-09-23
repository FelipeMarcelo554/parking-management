package com.api.parkingmanagement.repository;

import com.api.parkingmanagement.domain.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {

    Optional<RoleModel> findByRoleName(String name);
}
