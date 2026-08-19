package com.example.quanlytruonghoc.models.data.req.course;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseReq {
    @NotBlank(message = "Tiêu đề khóa học không được để trống")
    @Size(max = 255, message = "Tiêu đề không được vượt quá 255 ký tự")
    private String title;

    private String description;

    @NotNull(message = "ID giảng viên không được để trống")
    private Long teacherId;

    private String status;
}
