package com.example.quanlytruonghoc.models.services;

import com.example.quanlytruonghoc.models.data.entities.Course;
import com.example.quanlytruonghoc.models.data.req.CourseReq;

public interface ICourseService {
    Course createCourse(CourseReq req);
}
