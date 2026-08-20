package com.example.quanlytruonghoc.models.services.impl;

import com.example.quanlytruonghoc.exceptions.BadRequestException;
import com.example.quanlytruonghoc.exceptions.NotFoundException;
import com.example.quanlytruonghoc.models.data.dto.constants.RoleName;
import com.example.quanlytruonghoc.models.data.entities.Role;
import com.example.quanlytruonghoc.models.data.entities.School;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.mapper.SchoolMapper;
import com.example.quanlytruonghoc.models.data.req.school.SchoolReq;
import com.example.quanlytruonghoc.models.data.res.SchoolRes;
import com.example.quanlytruonghoc.models.repositories.IRoleRepository;
import com.example.quanlytruonghoc.models.repositories.ISchoolRepository;
import com.example.quanlytruonghoc.models.services.ISchoolService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements ISchoolService {
    private final IRoleRepository roleRepository;
    private final ISchoolRepository schoolRepository;
    private final SchoolMapper schoolMapper;

    @Override
    @Transactional
    public SchoolRes createSchool(User currentUser, SchoolReq req) {
        validateCanCreateSchool(currentUser);

        Role adminRole = roleRepository.findByRoleName(RoleName.ADMIN)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy role ADMIN"));

        School school = schoolMapper.toEntity(req);
        school.setCreatedBy(currentUser);
        school = schoolRepository.save(school);

        currentUser.setSchool(school);
        Set<Role> roles = new HashSet<>(currentUser.getRoles());
        roles.removeIf(role -> role.getRoleName() == RoleName.USER);
        roles.add(adminRole);
        currentUser.setRoles(roles);

        return schoolMapper.toResponse(school);
    }

    private void validateCanCreateSchool(User currentUser) {
        if (currentUser == null) {
            throw new BadRequestException("Người dùng không hợp lệ");
        }

        if (currentUser.getSchool() != null) {
            throw new BadRequestException("Bạn đã thuộc một trường và không thể tạo trường mới");
        }
    }

    @Override
    @Transactional
    public SchoolRes updateSchool(User currentUser, Long id, SchoolReq req) {
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy trường với ID: " + id));

        Set<RoleName> roleNames = roleRepository.findRoleNamesByUserId(currentUser.getId());
        boolean isSuperAdmin = roleNames.contains(RoleName.SUPER_ADMIN);
        boolean isAdminOfThisSchool = roleNames.contains(RoleName.ADMIN)
                && currentUser.getSchool() != null
                && currentUser.getSchool().getId().equals(school.getId());

        if (!isSuperAdmin && !isAdminOfThisSchool) {
            throw new BadRequestException("Bạn không có quyền cập nhật trường này");
        }

        schoolMapper.updateEntity(req, school);
        return schoolMapper.toResponse(schoolRepository.save(school));
    }

    @Override
    @Transactional
    public void deleteSchool(User currentUser, Long id) {
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy trường với ID: " + id));

        boolean isSuperAdmin = roleRepository.hasRole(currentUser.getId(), RoleName.SUPER_ADMIN);
        boolean isAdminOfThisSchool = roleRepository.hasRole(currentUser.getId(), RoleName.ADMIN)
                && currentUser.getSchool() != null
                && currentUser.getSchool().getId().equals(school.getId());

        if (!isSuperAdmin && !isAdminOfThisSchool) {
            throw new BadRequestException("Bạn không có quyền xóa trường này");
        }

        schoolRepository.delete(school);
    }

    @Override
    public List<SchoolRes> findAll() {
        return schoolMapper.toResponseList(schoolRepository.findAll());
    }
}
