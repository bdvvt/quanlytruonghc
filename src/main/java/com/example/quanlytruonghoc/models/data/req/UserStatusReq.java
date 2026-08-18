package com.example.quanlytruonghoc.models.data.req;

import com.example.quanlytruonghoc.models.constants.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserStatusReq {
    @NotNull(message = "Trạng thái status không được để trống")
    private UserStatus status;
}
