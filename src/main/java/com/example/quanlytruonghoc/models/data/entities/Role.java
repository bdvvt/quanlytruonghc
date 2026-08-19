package com.example.quanlytruonghoc.models.data.entities;

import com.example.quanlytruonghoc.models.data.dto.constants.RoleName;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "roles", uniqueConstraints = {
        @UniqueConstraint(columnNames = "role_name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private RoleName roleName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;
}
