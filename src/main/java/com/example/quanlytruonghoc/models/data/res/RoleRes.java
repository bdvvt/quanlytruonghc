package com.example.quanlytruonghoc.models.data.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleRes {
    private Long id;
    private String roleName;
}
