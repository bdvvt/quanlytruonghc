package com.example.quanlytruonghoc.controllers;

import com.example.quanlytruonghoc.models.constants.RoleName;
import com.example.quanlytruonghoc.models.constants.UserStatus;
import com.example.quanlytruonghoc.models.data.dto.wrapper.ApiResponse;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.req.UserPassReq;
import com.example.quanlytruonghoc.models.data.req.UserReq;
import com.example.quanlytruonghoc.models.data.req.UserStatusReq;
import com.example.quanlytruonghoc.models.data.req.UserUpRoleReq;
import com.example.quanlytruonghoc.models.services.IUserService;
import com.example.quanlytruonghoc.security.principal.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @PostMapping
    public ResponseEntity<?> addNewUser(@Valid @ModelAttribute UserReq req) {
        return ResponseEntity.status(201).body(
                ApiResponse.builder()
                        .message("Tạo mới người dùng thành công")
                        .code(201)
                        .data(userService.createUser(req))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id, @Valid @ModelAttribute UserReq req){
        User currentUser = userDetails.getUser();
        return ResponseEntity.status(200).body(
                ApiResponse.builder()
                        .message("Cập nhật người dùng thành công")
                        .code(200)
                        .data(userService.updateUser(currentUser, id,req))
                        .build()
        );
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> updateUserPassword(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long id, @Valid @ModelAttribute UserPassReq req) {
        User currentUser = customUserDetails.getUser();
        return ResponseEntity.status(200).body(
                ApiResponse.builder()
                        .message("Cập nhật mật khẩu người dùng thành công")
                        .code(200)
                        .data(userService.updateUserPassword(currentUser,id, req))
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .message("Lấy " + id + " người dùng thành công")
                        .code(200)
                        .data(userService.findById(id))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> dropout(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                ApiResponse.builder()
                        .message("Xóa người dùng thành công")
                        .code(204)
                        .data(null)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(value = "role", required = false) RoleName role,
                                     @RequestParam(value = "status", required = false) UserStatus status) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .message("Lấy người dùng thành công")
                        .code(200)
                        .data(userService.findAll(role, status))
                        .build()
        );
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @Valid @ModelAttribute UserUpRoleReq req){
        return ResponseEntity.status(200).body(
                ApiResponse.builder()
                        .message("cập nhật role người dùng thành công")
                        .code(200)
                        .data(userService.updateUserRole(id,req))
                        .build()
        );
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long id, @Valid @ModelAttribute UserStatusReq req) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .message("cập nhật trạng thái người dùng thành công")
                        .code(200)
                        .data(userService.updateUserStatus(id, req))
                        .build()
        );
    }


}
