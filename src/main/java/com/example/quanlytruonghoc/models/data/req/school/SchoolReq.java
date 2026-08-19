package com.example.quanlytruonghoc.models.data.req.school;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchoolReq {
    @NotBlank(message = "Tên trường không được để trống")
    @Size(max = 255, message = "Tên trường không được vượt quá 255 ký tự")
    private String name;

    private String description;

    private String address;

    private String phone;

    @Email(message = "Email không hợp lệ")
    private String email;
}
