package com.example.quanlytruonghoc.models.data.req;

import com.example.quanlytruonghoc.models.constants.CourseStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseStatusReq {
    @NotNull(message = "Trạng thái khóa học không được để trống")
    private CourseStatus status;
}

