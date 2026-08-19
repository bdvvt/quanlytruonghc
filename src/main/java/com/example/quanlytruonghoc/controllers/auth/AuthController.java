package com.example.quanlytruonghoc.controllers.auth;

import com.example.quanlytruonghoc.models.data.dto.wrapper.ApiResponse;
import com.example.quanlytruonghoc.models.data.req.auth.ActiveUserReq;
import com.example.quanlytruonghoc.models.data.req.auth.LoginReq;
import com.example.quanlytruonghoc.models.data.req.auth.RegisterReq;
import com.example.quanlytruonghoc.models.data.res.LoginRes;
import com.example.quanlytruonghoc.models.services.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/register")
    public ApiResponse<String> register(@Valid @ModelAttribute RegisterReq req) {
        authService.register(req);
        return ApiResponse.created(
                "Register User Successfully",
                "Register successfully"
        );
    }

    @PostMapping("/login")
    public ApiResponse<LoginRes> login(@Valid @ModelAttribute LoginReq req) {
        return ApiResponse.success(
                "Login User Successfully",
                authService.login(req)
        );
    }

    @PostMapping("/active-user")
    public ApiResponse<String> activeUser(@Valid @ModelAttribute ActiveUserReq req) {
        return ApiResponse.success(
                "Active User Successfully",
                authService.activeUser(req)
        );
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        authService.logout(request);
        return ApiResponse.success(
                "Đăng xuất thành công",
                null
        );
    }



}


