package com.example.quanlytruonghoc.models.services;

import com.example.quanlytruonghoc.models.data.entities.Course;
import com.example.quanlytruonghoc.models.data.req.CourseReq;
import com.example.quanlytruonghoc.models.data.req.CourseStatusReq;

import java.util.List;

public interface ICourseService {
    Course createCourse(CourseReq req);
    List<Course> findAllBySearch(String search);
    Course updateCourseStatus(Long courseId, CourseStatusReq req);
    Course updateCourse(Long id, CourseReq req);
    void deleteCourse(Long id);
}
