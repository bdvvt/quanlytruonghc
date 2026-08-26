package com.example.quanlytruonghoc.models.data.req.department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentReq {
    @NotBlank(message = "Tên phòng ban không được để trống")
    @Size(max = 255, message = "Tên phòng ban không được vượt quá 255 ký tự")
    private String name;

    private String description;

}
