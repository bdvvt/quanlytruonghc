package com.example.quanlytruonghoc.models.data.res;

import com.example.quanlytruonghoc.models.data.dto.constants.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseRes {
    private Long courseId;
    private String title;
    private String description;
    private UserRes teacher;
    private UserRes createdBy;
    private CourseStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}