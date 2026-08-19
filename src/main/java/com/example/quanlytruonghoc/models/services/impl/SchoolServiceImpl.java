package com.example.quanlytruonghoc.models.services.impl;

import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.req.school.SchoolReq;
import com.example.quanlytruonghoc.models.data.res.SchoolRes;
import com.example.quanlytruonghoc.models.services.ISchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements ISchoolService {
    @Override
    public SchoolRes createSchool(SchoolReq req) {
        return null;
    }

    @Override
    public SchoolRes updateSchool(User currentUser, Long id, SchoolReq req) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }

    @Override
    public List<SchoolRes> findAll() {
        return List.of();
    }
}
