package com.example.quanlytruonghoc.models.services;

import com.example.quanlytruonghoc.exceptions.BadRequestException;
import com.example.quanlytruonghoc.exceptions.NotFoundException;
import com.example.quanlytruonghoc.models.data.entities.Course;
import com.example.quanlytruonghoc.models.data.entities.Department;
import com.example.quanlytruonghoc.models.data.entities.School;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.req.course.CourseReq;
import com.example.quanlytruonghoc.models.data.req.course.CourseStatusReq;
import com.example.quanlytruonghoc.models.data.res.CourseRes;
import com.example.quanlytruonghoc.models.data.res.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICourseService {
    CourseRes createCourse(User currentUser, CourseReq req);
    PageResponse<CourseRes> findAllBySearch(User currentUser,Pageable pageable, String search);
    CourseRes updateCourseStatus(User currentUser,Long courseId, CourseStatusReq req);
    CourseRes updateCourse(User currentUser,Long id, CourseReq req);
    void deleteCourse(User currentUser,Long id);

}
