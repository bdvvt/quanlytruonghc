package com.example.quanlytruonghoc.models.services.impl;

import com.example.quanlytruonghoc.exceptions.BadRequestException;
import com.example.quanlytruonghoc.exceptions.NotFoundException;
import com.example.quanlytruonghoc.models.data.dto.constants.RoleName;
import com.example.quanlytruonghoc.models.data.dto.constants.UserStatus;
import com.example.quanlytruonghoc.models.data.entities.User;

import com.example.quanlytruonghoc.models.data.req.user.UserPassReq;
import com.example.quanlytruonghoc.models.data.req.user.UserReq;
import com.example.quanlytruonghoc.models.data.req.user.UserRoleReq;
import com.example.quanlytruonghoc.models.data.req.user.UserStatusReq;
import com.example.quanlytruonghoc.models.data.res.UserRes;
import com.example.quanlytruonghoc.models.data.mapper.UserMapper;
import com.example.quanlytruonghoc.models.repositories.IRoleRepository;
import com.example.quanlytruonghoc.models.repositories.IUserRepository;
import com.example.quanlytruonghoc.models.services.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserRes createUser(UserReq req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã được sử dụng!");
        }
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new RuntimeException("Tên đăng nhập đã được sử dụng!");
        }
        User user = userMapper.toEntity(req);
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRoles(new HashSet<>(roleRepository.findAllById(req.getRoleIds())));
        user.setStatus(UserStatus.INACTIVE);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        User deleteUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + id));
        if(deleteUser.getRoles().stream().anyMatch(role -> role.getRoleName() == RoleName.ADMIN)){
            throw new RuntimeException("Không thể xóa người dùng có quyền ADMIN!");
        }
        userRepository.delete(deleteUser);
    }

    @Override
    public List<UserRes> findAll(RoleName role, UserStatus status) {
       return userMapper.toResponseList(userRepository.findAllByRoleAndStatus(role,status));
    }

    @Override
    public UserRes findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + id));
        return userMapper.toResponse(user);
    }

    @Override
    public UserRes updateUser(User currentUser,Long id, UserReq req) {
        User updateUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + id));
        userRepository.findByEmail(req.getEmail()).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(id)) {
                throw new RuntimeException("Email đã được sử dụng bởi tài khoản khác!");
            }
        });
        userRepository.findByUsername(req.getUsername()).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(id)) {
                throw new RuntimeException("Tên đăng nhập đã được sử dụng bởi tài khoản khác!");
            }
        });
        if(updateUser.getRoles().stream().anyMatch(role -> role.getRoleName() == RoleName.ADMIN)){
            throw new RuntimeException("Không thể cập nhật thông tin của người dùng có quyền ADMIN!");
        }
        boolean isAdminOrSuperAdmin = roleRepository.hasAnyRole(currentUser.getId(), Set.of(RoleName.ADMIN, RoleName.SUPER_ADMIN));
        boolean isOwner = updateUser.getId().equals(currentUser.getId());
        if (!isAdminOrSuperAdmin && !isOwner) {
            throw new BadRequestException("Bạn không có quyền chỉnh sửa mật khẩu người dùng này");
        }
        userMapper.updateEntity(req, updateUser);
        updateUser.setRoles(new HashSet<>(roleRepository.findAllById(req.getRoleIds())));
        updateUser.setStatus(UserStatus.INACTIVE);
        return userMapper.toResponse(userRepository.save(updateUser));
    }

    @Override
    public UserRes updateUserPassword(User currentUser, Long id, UserPassReq req) {
        User updateUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + id));
        boolean isAdminOrSuperAdmin = roleRepository.hasAnyRole(currentUser.getId(), Set.of(RoleName.ADMIN, RoleName.SUPER_ADMIN));
        boolean isOwner = updateUser.getId().equals(currentUser.getId());
        if (!isAdminOrSuperAdmin && !isOwner) {
            throw new BadRequestException("Bạn không có quyền chỉnh sửa mật khẩu người dùng này");
        }
        updateUser.setPassword(passwordEncoder.encode(req.getPassword()));
        return userMapper.toResponse(userRepository.save(updateUser));
    }

    @Override
    public UserRes updateUserRole(User currentUser,Long id, UserRoleReq req) {
        User updateUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + id));
        boolean isSuperAdmin = roleRepository.hasRole(currentUser.getId(),RoleName.SUPER_ADMIN);
        boolean targetIsProtected = roleRepository.hasAnyRole(id, Set.of(RoleName.ADMIN, RoleName.SUPER_ADMIN));
        if (targetIsProtected && !isSuperAdmin) {
            throw new RuntimeException("Bạn không có quyền update người dùng này!");
        }
        updateUser.setRoles(new HashSet<>(roleRepository.findAllById(req.getRoleIds())));
        return userMapper.toResponse(userRepository.save(updateUser));
    }

    @Override
    public UserRes updateUserStatus(User currentUser,Long id, UserStatusReq req) {
        User updateUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + id));
        boolean isSuperAdmin = roleRepository.hasRole(currentUser.getId(),RoleName.SUPER_ADMIN);
        boolean targetIsProtected = roleRepository.hasAnyRole(id, Set.of(RoleName.ADMIN, RoleName.SUPER_ADMIN));
        if (targetIsProtected && !isSuperAdmin) {
            throw new RuntimeException("Bạn không có quyền update người dùng này!");
        }
        updateUser.setStatus(req.getStatus());
        return userMapper.toResponse(userRepository.save(updateUser));
    }
}