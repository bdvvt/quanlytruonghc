package com.example.quanlytruonghoc.models.services;

import com.example.quanlytruonghoc.models.data.dto.constants.RoleName;
import com.example.quanlytruonghoc.models.data.dto.constants.UserStatus;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.req.school.SchoolReq;
import com.example.quanlytruonghoc.models.data.req.user.UserReq;
import com.example.quanlytruonghoc.models.data.res.SchoolRes;
import com.example.quanlytruonghoc.models.data.res.UserRes;

import java.util.List;

public interface ISchoolService {
    SchoolRes createSchool(User currentUser,SchoolReq req);
    SchoolRes updateSchool(User currentUser, Long id, SchoolReq req);
    void deleteSchool(User currentUser,Long id);
    List<SchoolRes> findAll();
}
