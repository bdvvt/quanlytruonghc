package com.example.quanlytruonghoc.models.data.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRes {
    private String accessToken;
    @Builder.Default
    private String type = "Bearer";
    private Set<String> roles;
}
