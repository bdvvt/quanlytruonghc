package com.example.quanlytruonghoc.controllers.user;

import com.example.quanlytruonghoc.models.data.dto.constants.RoleName;
import com.example.quanlytruonghoc.models.data.dto.constants.UserStatus;
import com.example.quanlytruonghoc.models.data.dto.wrapper.ApiResponse;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.req.user.UserPassReq;
import com.example.quanlytruonghoc.models.data.req.user.UserReq;
import com.example.quanlytruonghoc.models.data.req.user.UserStatusReq;
import com.example.quanlytruonghoc.models.data.req.user.UserRoleReq;
import com.example.quanlytruonghoc.models.data.res.PageResponse;
import com.example.quanlytruonghoc.models.data.res.UserRes;
import com.example.quanlytruonghoc.models.services.IUserService;
import com.example.quanlytruonghoc.security.principal.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @PostMapping
    public ApiResponse<UserRes> addNewUser(@AuthenticationPrincipal CustomUserDetails userDetails,@Valid @ModelAttribute UserReq req) {
        User currentUser = userDetails.getUser();
        return ApiResponse.created(
                "Tạo mới người dùng thành công",
                userService.createUser(currentUser,req)
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<UserRes> updateUser(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id, @Valid @ModelAttribute UserReq req){
        User currentUser = userDetails.getUser();
        return ApiResponse.success(
                "Cập nhật người dùng thành công",
                userService.updateUser(currentUser, id, req)
        );
    }

    @PutMapping("/{id}/password")
    public ApiResponse<UserRes> updateUserPassword(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long id, @Valid @ModelAttribute UserPassReq req) {
        User currentUser = customUserDetails.getUser();
        return ApiResponse.success(
                "Cập nhật mật khẩu người dùng thành công",
                userService.updateUserPassword(currentUser, id, req)
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<UserRes> findById(@PathVariable Long id) {
        return  ApiResponse.success(
                "Lấy " + id + " người dùng thành công",
                userService.findById(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> dropout(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ApiResponse<PageResponse<UserRes>> findAll(
            @PageableDefault(
                    page = 0,
                    size = 5,
                    sort = "title",
                    direction = Sort.Direction.ASC
            ) Pageable pageable,
            @RequestParam(value = "role", required = false) RoleName role,
            @RequestParam(value = "status", required = false) UserStatus status,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User currentUser = customUserDetails.getUser();
        return ApiResponse.success(
                "Lấy người dùng thành công",
                userService.findAll(pageable,currentUser,role, status)
        );
    }

    @PutMapping("/{id}/role")
    public ApiResponse<UserRes> updateUserRole(@AuthenticationPrincipal CustomUserDetails customUserDetails,@PathVariable Long id, @Valid @ModelAttribute UserRoleReq req){
        User currentUser = customUserDetails.getUser();
        return ApiResponse.success(
                "Cập nhật role người dùng thành công",
                userService.updateUserRole(currentUser,id, req)
        );
    }

    @PutMapping("/{id}/status")
    public ApiResponse<UserRes> updateUserStatus(@AuthenticationPrincipal CustomUserDetails customUserDetails,@PathVariable Long id, @Valid @ModelAttribute UserStatusReq req) {
        User currentUser = customUserDetails.getUser();
        return ApiResponse.success(
                "Cập nhật trạng thái người dùng thành công",
                userService.updateUserStatus(currentUser,id, req)
        );
    }


}
