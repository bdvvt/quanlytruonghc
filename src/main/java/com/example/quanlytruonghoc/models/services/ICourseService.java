package com.example.quanlytruonghoc.models.services;

import com.example.quanlytruonghoc.models.data.entities.Course;
import com.example.quanlytruonghoc.models.data.req.course.CourseReq;
import com.example.quanlytruonghoc.models.data.req.course.CourseStatusReq;
import com.example.quanlytruonghoc.models.data.res.CourseRes;

import java.util.List;

public interface ICourseService {
    CourseRes createCourse(CourseReq req);
    List<CourseRes> findAllBySearch(String search);
    CourseRes updateCourseStatus(Long courseId, CourseStatusReq req);
    CourseRes updateCourse(Long id, CourseReq req);
    void deleteCourse(Long id);
}
