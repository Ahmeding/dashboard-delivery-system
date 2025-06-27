package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.Role;
import com.hitecart.deliverysystem.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleType name);

    boolean existsByName(String roleName);
}
