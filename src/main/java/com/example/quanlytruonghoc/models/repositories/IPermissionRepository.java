package com.example.quanlytruonghoc.models.repositories;

import com.example.quanlytruonghoc.models.data.dto.constants.PermissionName;
import com.example.quanlytruonghoc.models.data.dto.constants.RoleName;
import com.example.quanlytruonghoc.models.data.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPermissionRepository extends JpaRepository<Permission,Long> {
    boolean existsByPermissionName(PermissionName name);
}
