package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.Role;
import com.hitecart.deliverysystem.model.enums.RoleType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleType name);

    boolean existsByName(String roleName);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Role r where r.name = :name")
    Optional<Role> findByNameForUpdate(@Param("name") RoleType name);
}
