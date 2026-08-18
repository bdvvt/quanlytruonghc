package com.example.quanlytruonghoc.models.repositories;


import com.example.quanlytruonghoc.models.constants.RoleName;
import com.example.quanlytruonghoc.models.data.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(RoleName roleName);
}

