package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.User;
import com.hitecart.deliverysystem.model.enums.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query("""
            SELECT u
            FROM User u
            """)
    Page<User> findAllUsers(Pageable pageable);

    boolean existsByPhoneNbr(String phoneNbr);

    Boolean existsByEmailIgnoreCase(String email);

    @Query("""
            SELECT u
            FROM User u
            JOIN  u.roles r
            WHERE r.id=:roleId
            """)
    List<User> findAllByRoleId(@Param("roleId") Integer roleId);

    @Query("""
            SELECT case when count(u) >0 then true else false end
            FROM User u join u.roles r
            where r.name = :role
            """)
    boolean existByRole(@Param("role") RoleType role);
}
