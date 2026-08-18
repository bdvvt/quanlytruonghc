package com.example.quanlytruonghoc.models.data.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpRoleReq {
    @NotEmpty(message = "Danh sách role không được để trống")
    private Set<Long> roleIds;
}
