package com.example.quanlytruonghoc.models.services.impl;

import com.example.quanlytruonghoc.exceptions.BadRequestException;
import com.example.quanlytruonghoc.exceptions.DataConflictException;
import com.example.quanlytruonghoc.exceptions.NotFoundException;
import com.example.quanlytruonghoc.models.data.dto.constants.RoleName;
import com.example.quanlytruonghoc.models.data.entities.Department;
import com.example.quanlytruonghoc.models.data.entities.School;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.mapper.DepartmentMapper;
import com.example.quanlytruonghoc.models.data.req.department.DepartmentReq;
import com.example.quanlytruonghoc.models.data.res.DepartmentRes;
import com.example.quanlytruonghoc.models.data.res.PageResponse;
import com.example.quanlytruonghoc.models.repositories.IDepartmentRepository;
import com.example.quanlytruonghoc.models.repositories.IRoleRepository;
import com.example.quanlytruonghoc.models.repositories.ISchoolRepository;
import com.example.quanlytruonghoc.models.services.IDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements IDepartmentService {
    private final IDepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final ISchoolRepository schoolRepository;
    private final IRoleRepository roleRepository;

    @Override
    public DepartmentRes createDepartment(User currentUser, DepartmentReq req) {
        if (departmentRepository.findByName(req.getName()).isPresent()) {
            throw new DataConflictException("Tên phòng ban đã được sử dụng");
        }
        School school = currentUser.getSchool();
        if (school == null) {
            throw new NotFoundException("Không tìm thấy trường được gán cho người dùng hiện tại");
        }
        Department department = departmentMapper.toEntity(req);
        department.setSchool(school);
        department.setCreatedBy(currentUser);
        return departmentMapper.toResponse(departmentRepository.save(department));
    }

    @Override
    public DepartmentRes updateDepartment(User currentUser, Long id, DepartmentReq req) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy phòng ban này" ));
        School school = currentUser.getSchool();
        Set<RoleName> roleNames = roleRepository.findRoleNamesByUserId(currentUser.getId());
        boolean isSuperAdmin = roleNames.contains(RoleName.SUPER_ADMIN);
        departmentRepository.findByName(req.getName()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new DataConflictException("Tên phòng ban đã được sử dụng bởi tài khoản khác!");
            }
        });
        if (!isSuperAdmin && school == null) {
            throw new NotFoundException("Không tìm thấy trường được gán cho người dùng hiện tại");
        }
        boolean sameSchool = school != null && department.getSchool() != null && school.getId().equals(department.getSchool().getId());

        boolean sameDepartment = currentUser.getDepartment() != null && currentUser.getDepartment().getId().equals(department.getId());
        if (!isSuperAdmin ) {
            if (!sameSchool) {
                throw new BadRequestException("Bạn không có quyền cập nhật phòng ban của trường khác");
            }
            if (!sameDepartment) {
                throw new BadRequestException("Bạn không có quyền cập nhật phòng ban khác");
            }
        }
        departmentMapper.updateEntity(req,department);
        return departmentMapper.toResponse(departmentRepository.save(department));
    }

    @Override
    public void deleteDepartment(User currentUser, Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy phòng ban này "));
        School school = department.getSchool();
        if (school == null) {
            throw new NotFoundException("Không tìm thấy trường trực thuộc phòng ban này");
        }
        if (currentUser.getSchool() == null) {
            throw new NotFoundException("Không tìm thấy trường được gán cho người dùng hiện tại");
        }
        boolean sameSchool = currentUser.getSchool().getId().equals(school.getId());
        if (!sameSchool) {
            throw new BadRequestException("Bạn không có quyền xóa phòng ban của trường khác");
        }
        boolean sameDepartment = currentUser.getDepartment() != null && currentUser.getDepartment().getId().equals(department.getId());
        if (!sameDepartment) {
            throw new BadRequestException("Bạn không có quyền xóa phòng ban khác");
        }
        departmentRepository.delete(department);

    }

    @Override
    public PageResponse<DepartmentRes> findAll(User currentUser,Pageable pageable) {
        School  school = currentUser.getSchool();
        if (school == null) {
            throw new NotFoundException("Không tìm thấy trường được gán cho người dùng hiện tại");
        }
        return departmentMapper.toPageResponse(departmentRepository.findAllBySchool(school,pageable));
    }
}
