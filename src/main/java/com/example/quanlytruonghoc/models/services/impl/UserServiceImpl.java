package com.example.quanlytruonghoc.models.services.impl;

import com.example.quanlytruonghoc.exceptions.BadRequestException;
import com.example.quanlytruonghoc.exceptions.NotFoundException;
import com.example.quanlytruonghoc.models.constants.RoleName;
import com.example.quanlytruonghoc.models.constants.UserStatus;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.req.UserPassReq;
import com.example.quanlytruonghoc.models.data.req.UserReq;
import com.example.quanlytruonghoc.models.data.req.UserStatusReq;
import com.example.quanlytruonghoc.models.data.req.UserUpRoleReq;
import com.example.quanlytruonghoc.models.mapper.UserMapper;
import com.example.quanlytruonghoc.models.repositories.IRoleRepository;
import com.example.quanlytruonghoc.models.repositories.IUserRepository;
import com.example.quanlytruonghoc.models.services.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public User createUser(UserReq req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã được sử dụng!");
        }
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new RuntimeException("Tên đăng nhập đã được sử dụng!");
        }

        log.info("Saving new User entity to database for username: {}", req.getUsername());
        User user = userMapper.toEntity(req);
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRoles(new HashSet<>(roleRepository.findAllById(req.getRoleIds())));
        user.setStatus(UserStatus.INACTIVE);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user record with ID: {}", id);
        User deleteUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + id));
        if(deleteUser.getRoles().stream().anyMatch(role -> role.getRoleName() == RoleName.ADMIN)){
            throw new RuntimeException("Không thể xóa người dùng có quyền ADMIN!");
        }
        userRepository.delete(deleteUser);
    }

    @Override
    public List<User> findAll(RoleName role, UserStatus status) {
        log.info("Fetching users with role: {} and status: {}", role, status);
        return userRepository.findAllByRoleAndStatus(role, status);
    }

    @Override
    public User findById(Long id) {
        log.info("Fetching user with ID: {}", id);
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + id));
    }

    @Override
    public User updateUser(User currentUser,Long id, UserReq req) {
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
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getRoleName() == RoleName.ADMIN);
        boolean isOwner = updateUser.getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new BadRequestException("Bạn không có quyền chỉnh sửa thông tin người dùng này");
        }
        log.info("Updating user record with ID: {}", id);
        userMapper.updateEntity(req, updateUser);
        updateUser.setRoles(new HashSet<>(roleRepository.findAllById(req.getRoleIds())));
        updateUser.setStatus(UserStatus.INACTIVE);
        return userRepository.save(updateUser);
    }

    @Override
    public User updateUserPassword(User currentUser, Long id, UserPassReq req) {
        log.info("Updating password for user ID: {}", id);
        User updateUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + id));
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getRoleName() == RoleName.ADMIN);
        boolean isOwner = updateUser.getId().equals(currentUser.getId());
        if (!isAdmin && !isOwner) {
            throw new BadRequestException("Bạn không có quyền chỉnh sửa mat khau người dùng này");
        }
        updateUser.setPassword(passwordEncoder.encode(req.getPassword()));
        return userRepository.save(updateUser);
    }

    @Override
    public User updateUserRole(Long id, UserUpRoleReq req) {
        User updateUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + id));
        if(updateUser.getRoles().stream().anyMatch(role -> role.getRoleName() == RoleName.ADMIN)){
            throw new RuntimeException("Không thể cập nhật thông tin của người dùng có quyền ADMIN!");
        }
        log.info("Updating user role for ID: {}", id);
        updateUser.setRoles(new HashSet<>(roleRepository.findAllById(req.getRoleIds())));
        return userRepository.save(updateUser);
    }

    @Override
    public User updateUserStatus(Long id, UserStatusReq req) {
        User updateUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + id));
        if(updateUser.getRoles().stream().anyMatch(role -> role.getRoleName() == RoleName.ADMIN)){
            throw new RuntimeException("Không thể cập nhật thông tin của người dùng có quyền ADMIN!");
        }
        log.info("Updating user status for ID: {} to {}", id, req.getStatus());
        updateUser.setStatus(req.getStatus());
        return userRepository.save(updateUser);
    }
}
