package com.example.quanlytruonghoc.models.data.res;

import com.example.quanlytruonghoc.models.data.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRes {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private Set<RoleRes> roles;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}