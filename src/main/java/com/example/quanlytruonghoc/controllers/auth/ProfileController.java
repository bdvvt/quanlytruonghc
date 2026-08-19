package com.example.quanlytruonghoc.controllers.auth;

import com.example.quanlytruonghoc.models.data.dto.wrapper.ApiResponse;
import com.example.quanlytruonghoc.models.data.res.UserRes;
import com.example.quanlytruonghoc.models.services.IUserService;
import com.example.quanlytruonghoc.security.principal.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ProfileController {
    private final IUserService userService;
    @GetMapping("/me")
    public ApiResponse<UserRes> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.success(
                "Lấy thông tin cá nhân thành công",
                userService.findById(userDetails.getUser().getId())
        );
    }
}
