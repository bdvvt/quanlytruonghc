package com.example.quanlytruonghoc.models.services;

import com.example.quanlytruonghoc.models.constants.RoleName;
import com.example.quanlytruonghoc.models.constants.UserStatus;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.req.UserPassReq;
import com.example.quanlytruonghoc.models.data.req.UserReq;
import com.example.quanlytruonghoc.models.data.req.UserStatusReq;
import com.example.quanlytruonghoc.models.data.req.UserUpRoleReq;

import java.util.List;

public interface IUserService {
    User createUser(UserReq req);
    User updateUser(User currentUser,Long id, UserReq req);
    User updateUserPassword(User currentUser,Long id, UserPassReq req);
    User updateUserRole(Long id, UserUpRoleReq req);
    User findById(Long id);
    void deleteUser(Long id);
    List<User> findAll(RoleName role, UserStatus status);
    User updateUserStatus(Long id, UserStatusReq req);
}
