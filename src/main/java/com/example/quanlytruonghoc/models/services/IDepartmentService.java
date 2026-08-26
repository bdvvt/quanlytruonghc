package com.example.quanlytruonghoc.models.services;

import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.req.department.DepartmentReq;
import com.example.quanlytruonghoc.models.data.res.DepartmentRes;
import com.example.quanlytruonghoc.models.data.res.PageResponse;
import org.springframework.data.domain.Pageable;

public interface IDepartmentService {
    DepartmentRes createDepartment(User currentUser, DepartmentReq req);
    DepartmentRes updateDepartment(User currentUser, Long id, DepartmentReq req);
    void deleteDepartment(User currentUser,Long id);
    PageResponse<DepartmentRes> findAll(User currentUser,Pageable pageable);
}
