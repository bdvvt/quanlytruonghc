package com.example.quanlytruonghoc.models.repositories;


import com.example.quanlytruonghoc.models.data.dto.constants.RoleName;
import com.example.quanlytruonghoc.models.data.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface IRoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(RoleName roleName);
    boolean existsByRoleName(RoleName name);

    @Query("""
        SELECT r.roleName
        FROM User u
        JOIN u.roles r
        WHERE u.id = :userId
        """)
    Set<RoleName> findRoleNamesByUserId(@Param("userId") Long userId);
}

