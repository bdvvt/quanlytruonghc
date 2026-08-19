package com.example.quanlytruonghoc.configs;

import com.example.quanlytruonghoc.models.data.dto.constants.PermissionName;
import com.example.quanlytruonghoc.models.data.dto.constants.RoleName;
import com.example.quanlytruonghoc.models.data.entities.Permission;
import com.example.quanlytruonghoc.models.data.entities.Role;
import com.example.quanlytruonghoc.models.repositories.IPermissionRepository;
import com.example.quanlytruonghoc.models.repositories.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleDataInitializer implements CommandLineRunner {
    private final IRoleRepository roleRepository;
    private final IPermissionRepository permissionRepository;

    @Override
    public void run(String... args) throws Exception {
        for (RoleName roleName : RoleName.values()) {
            if (!roleRepository.existsByRoleName(roleName)) {
                roleRepository.save(
                        Role.builder()
                                .roleName(roleName)
                                .build()
                );
            }
        }
        for (PermissionName permissionName : PermissionName.values()) {
            if (!permissionRepository.existsByPermissionName(permissionName)) {
                permissionRepository.save(
                        Permission.builder()
                                .permissionName(permissionName)
                                .build()
                );
            }
        }
    }
}
