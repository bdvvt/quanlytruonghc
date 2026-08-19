package com.example.quanlytruonghoc.models.data.entities;

import com.example.quanlytruonghoc.models.data.dto.constants.PermissionName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(
        name = "permissions",
        uniqueConstraints = @UniqueConstraint(columnNames = "permission_name")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_name")
    private PermissionName permissionName;

    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles;

}