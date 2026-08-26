package com.example.quanlytruonghoc.models.services.impl;

import com.example.quanlytruonghoc.exceptions.BadRequestException;
import com.example.quanlytruonghoc.exceptions.NotFoundException;
import com.example.quanlytruonghoc.models.data.dto.constants.RoleName;
import com.example.quanlytruonghoc.models.data.entities.Role;
import com.example.quanlytruonghoc.models.data.entities.School;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.mapper.SchoolMapper;
import com.example.quanlytruonghoc.models.data.req.school.SchoolReq;
import com.example.quanlytruonghoc.models.data.res.PageResponse;
import com.example.quanlytruonghoc.models.data.res.SchoolRes;
import com.example.quanlytruonghoc.models.repositories.IRoleRepository;
import com.example.quanlytruonghoc.models.repositories.ISchoolRepository;
import com.example.quanlytruonghoc.models.repositories.IUserRepository;
import com.example.quanlytruonghoc.models.services.ISchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements ISchoolService {
    private final IRoleRepository roleRepository;
    private final ISchoolRepository schoolRepository;
    private final IUserRepository userRepository;
    private final SchoolMapper schoolMapper;

    @Override
    public SchoolRes createSchool(User currentUser,SchoolReq req) {
        if (currentUser.getSchool() != null) {
            throw new BadRequestException("Bạn không tạo được trường mới");
        }
        School school = schoolMapper.toEntity(req);
        school.setCreatedBy(currentUser);
        school = schoolRepository.save(school);

        currentUser.setSchool(school);
        Set<Role> roles = new HashSet<>(currentUser.getRoles());
        roles.removeIf(role -> role.getRoleName() == RoleName.USER);
        roles.add(
                roleRepository.findByRoleName(RoleName.ADMIN)
                        .orElseThrow(() -> new NotFoundException("Không tìm thấy role "))
        );
        currentUser.setRoles(roles);
        userRepository.save(currentUser);

        return schoolMapper.toResponse(school);
    }

    @Override
    public SchoolRes updateSchool(User currentUser, Long id, SchoolReq req) {
        School school = schoolRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy trường với ID: " + id));
        Set<RoleName> roleNames = roleRepository.findRoleNamesByUserId(currentUser.getId());
        boolean isSuperAdmin = roleNames.contains(RoleName.SUPER_ADMIN);
        boolean isAdmin = roleNames.contains(RoleName.ADMIN);
        boolean isAdminOfThisSchool = isAdmin
                && currentUser.getSchool() != null
                && currentUser.getSchool().getId().equals(school.getId());

        if (!isSuperAdmin && !isAdminOfThisSchool) {
            throw new BadRequestException("Bạn không có quyền cập nhật trường này");
        }
        schoolMapper.updateEntity(req, school);
        return schoolMapper.toResponse(schoolRepository.save(school));
    }

    @Override
    public void deleteSchool(User currentUser,Long id) {
        School school = schoolRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy trường với ID: " + id));
        Set<RoleName> roleNames = roleRepository.findRoleNamesByUserId(currentUser.getId());
        boolean isSuperAdmin = roleNames.contains(RoleName.SUPER_ADMIN);
        boolean isAdmin = roleNames.contains(RoleName.ADMIN);
        boolean isAdminOfThisSchool = isAdmin
                && currentUser.getSchool() != null
                && currentUser.getSchool().getId().equals(school.getId());
        if (!isSuperAdmin && !isAdminOfThisSchool) {
            throw new BadRequestException("Bạn không có quyền xóa trường này");
        }
        schoolRepository.delete(school);
    }

    @Override
    public PageResponse<SchoolRes> findAll(Pageable pageable) {
        return schoolMapper.toPageResponse(schoolRepository.findAll(pageable));
    }
}
