package com.example.quanlytruonghoc.models.services;

import com.example.quanlytruonghoc.models.data.dto.constants.RoleName;
import com.example.quanlytruonghoc.models.data.dto.constants.UserStatus;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.req.user.UserPassReq;
import com.example.quanlytruonghoc.models.data.req.user.UserReq;
import com.example.quanlytruonghoc.models.data.req.user.UserStatusReq;
import com.example.quanlytruonghoc.models.data.req.user.UserRoleReq;
import com.example.quanlytruonghoc.models.data.res.PageResponse;
import com.example.quanlytruonghoc.models.data.res.UserRes;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
    UserRes createUser(User currentUser,UserReq req);
    UserRes updateUser(User currentUser,Long id, UserReq req);
    UserRes updateUserPassword(User currentUser,Long id, UserPassReq req);
    UserRes updateUserRole(User currentUser,Long id, UserRoleReq req);
    UserRes updateUserStatus(User currentUser,Long id, UserStatusReq req);
    UserRes findById(Long id);
    void deleteUser(Long id);
    PageResponse<UserRes> findAll(Pageable pageable, User currentUser, RoleName role, UserStatus status);
}
