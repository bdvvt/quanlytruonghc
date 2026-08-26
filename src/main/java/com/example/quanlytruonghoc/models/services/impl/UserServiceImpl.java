package com.example.quanlytruonghoc.models.services.impl;

import com.example.quanlytruonghoc.exceptions.BadRequestException;
import com.example.quanlytruonghoc.exceptions.DataConflictException;
import com.example.quanlytruonghoc.exceptions.NotFoundException;
import com.example.quanlytruonghoc.models.data.dto.constants.RoleName;
import com.example.quanlytruonghoc.models.data.dto.constants.UserStatus;
import com.example.quanlytruonghoc.models.data.entities.Role;
import com.example.quanlytruonghoc.models.data.entities.User;

import com.example.quanlytruonghoc.models.data.req.user.UserPassReq;
import com.example.quanlytruonghoc.models.data.req.user.UserReq;
import com.example.quanlytruonghoc.models.data.req.user.UserRoleReq;
import com.example.quanlytruonghoc.models.data.req.user.UserStatusReq;
import com.example.quanlytruonghoc.models.data.res.PageResponse;
import com.example.quanlytruonghoc.models.data.res.UserRes;
import com.example.quanlytruonghoc.models.data.mapper.UserMapper;
import com.example.quanlytruonghoc.models.repositories.IRoleRepository;
import com.example.quanlytruonghoc.models.repositories.IUserRepository;
import com.example.quanlytruonghoc.models.services.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public UserRes createUser(User currentUser,UserReq req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new DataConflictException("Email đã được sử dụng!");
        }
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new DataConflictException("Tên đăng nhập đã được sử dụng!");
        }
        Set<RoleName> currentUserRoles = roleRepository.findRoleNamesByUserId(currentUser.getId());
        boolean isSuperAdmin = currentUserRoles.contains(RoleName.SUPER_ADMIN);
        boolean isAdmin = currentUserRoles.contains(RoleName.ADMIN);
        User user = userMapper.toEntity(req);
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        RoleName roleName;
        if (isAdmin && !isSuperAdmin) {
            user.setSchool(currentUser.getSchool());
            roleName = RoleName.STUDENT;

        } else if (isSuperAdmin) {
            user.setSchool(null);
            roleName = RoleName.USER;
        } else {
            throw new BadRequestException("Bạn không có quyền tạo người dùng");
        }
        Role role = roleRepository.findByRoleName(roleName).orElseThrow(() -> new NotFoundException("Không tìm thấy role: " + roleName));
        user.setRoles(Set.of(role));
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        User deleteUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + id));
        if(deleteUser.getRoles().stream().anyMatch(role -> role.getRoleName() == RoleName.ADMIN)){
            throw new BadRequestException("Không thể xóa người dùng có quyền ADMIN!");
        }
        userRepository.delete(deleteUser);
    }

    @Override
    public PageResponse<UserRes> findAll(Pageable pageable, User currentUser, RoleName role, UserStatus status) {
        Set<RoleName> currentUserRoles = roleRepository.findRoleNamesByUserId(currentUser.getId());
        boolean isSuperAdmin = currentUserRoles.contains(RoleName.SUPER_ADMIN);
        boolean isAdmin = currentUserRoles.contains(RoleName.ADMIN);
        if (isAdmin && !isSuperAdmin) {
            if (currentUser.getSchool() == null) {
                throw new NotFoundException("Không tìm thấy trường được gán cho người dùng hiện tại");
            }
            Long school = currentUser.getSchool().getId();
            return userMapper.toPageResponse(userRepository.findAllBySchoolIdAndRoleAndStatus(school, role,status,pageable));

        }  if (isSuperAdmin) {
            return userMapper.toPageResponse(userRepository.findAllByRoleAndStatus(role,status,pageable));
        }
        throw new NotFoundException("Không tìm thấy dữ liệu người dùng phù hợp với quyền hiện tại");
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
                throw new DataConflictException("Email đã được sử dụng bởi tài khoản khác!");
            }
        });
        userRepository.findByUsername(req.getUsername()).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(id)) {
                throw new DataConflictException("Tên đăng nhập đã được sử dụng bởi tài khoản khác!");
            }
        });
        Set<RoleName> roleNames = roleRepository.findRoleNamesByUserId(currentUser.getId());
        boolean isSuperAdmin = roleNames.contains(RoleName.SUPER_ADMIN);
        boolean isAdmin = roleNames.contains(RoleName.ADMIN);
        boolean isOwner = updateUser.getId().equals(currentUser.getId());
        boolean targetIsAdmin = roleRepository.findRoleNamesByUserId(updateUser.getId()).contains(RoleName.ADMIN);
        if (targetIsAdmin) {
            throw new BadRequestException("Không thể cập nhật thông tin của người dùng có quyền ADMIN!");
        }
        if (!isSuperAdmin && !isOwner && !isAdmin) {
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
        Set<RoleName> currentUserRoles = roleRepository.findRoleNamesByUserId(currentUser.getId());
        Set<RoleName> targetUserRoles = roleRepository.findRoleNamesByUserId(updateUser.getId());
        boolean isSuperAdmin = currentUserRoles.contains(RoleName.SUPER_ADMIN);
        boolean isAdmin = currentUserRoles.contains(RoleName.ADMIN);
        boolean isOwner = currentUser.getId().equals(updateUser.getId());
        boolean targetIsAdmin = targetUserRoles.contains(RoleName.ADMIN);
        boolean targetIsSuperAdmin = targetUserRoles.contains(RoleName.SUPER_ADMIN);
        if (targetIsSuperAdmin && !isSuperAdmin) {
            throw new BadRequestException("Bạn không có quyền chỉnh sửa SUPER_ADMIN");
        }
        if (targetIsAdmin && !isSuperAdmin) {
            throw new BadRequestException("Chỉ SUPER_ADMIN mới có quyền chỉnh sửa ADMIN");
        }
        if (!isSuperAdmin && !isAdmin && !isOwner) {
            throw new BadRequestException("Bạn không có quyền chỉnh sửa mật khẩu người dùng này");
        }
        updateUser.setPassword(passwordEncoder.encode(req.getPassword()));
        return userMapper.toResponse(userRepository.save(updateUser));
    }

    @Override
    public UserRes updateUserRole(User currentUser,Long id, UserRoleReq req) {
        User updateUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + id));
        Set<RoleName> roleNames = roleRepository.findRoleNamesByUserId(currentUser.getId());
        Set<RoleName> targetUserRoles = roleRepository.findRoleNamesByUserId(updateUser.getId());
        boolean isSuperAdmin = roleNames.contains(RoleName.SUPER_ADMIN);
        boolean targetIsAdmin = targetUserRoles.contains(RoleName.ADMIN);
        boolean targetIsSuperAdmin = targetUserRoles.contains(RoleName.SUPER_ADMIN);
        if (targetIsAdmin && !isSuperAdmin) {
            throw new BadRequestException("Bạn không có quyền cập nhật người dùng ADMIN!");
        }
        if (targetIsSuperAdmin && !isSuperAdmin) {
            throw new BadRequestException("Bạn không có quyền cập nhật người dùng SUPER_ADMIN!");
        }
        updateUser.setRoles(new HashSet<>(roleRepository.findAllById(req.getRoleIds())));
        return userMapper.toResponse(userRepository.save(updateUser));
    }

    @Override
    public UserRes updateUserStatus(User currentUser,Long id, UserStatusReq req) {
        User updateUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + id));
        Set<RoleName> roleNames = roleRepository.findRoleNamesByUserId(currentUser.getId());
        Set<RoleName> targetUserRoles = roleRepository.findRoleNamesByUserId(updateUser.getId());
        boolean isSuperAdmin = roleNames.contains(RoleName.SUPER_ADMIN);
        boolean targetIsAdmin = targetUserRoles.contains(RoleName.ADMIN);
        boolean targetIsSuperAdmin = targetUserRoles.contains(RoleName.SUPER_ADMIN);
        if (targetIsAdmin && !isSuperAdmin) {
            throw new BadRequestException("Bạn không có quyền cập nhật người dùng ADMIN!");
        }
        if (targetIsSuperAdmin && !isSuperAdmin) {
            throw new BadRequestException("Bạn không có quyền cập nhật người dùng SUPER_ADMIN!");
        }
        updateUser.setStatus(req.getStatus());
        return userMapper.toResponse(userRepository.save(updateUser));
    }
}
