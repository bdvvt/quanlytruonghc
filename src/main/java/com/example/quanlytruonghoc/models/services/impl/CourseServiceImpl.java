package com.example.quanlytruonghoc.models.services.impl;

import com.example.quanlytruonghoc.exceptions.NotFoundException;
import com.example.quanlytruonghoc.models.data.dto.constants.CourseStatus;
import com.example.quanlytruonghoc.models.data.entities.Course;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.req.course.CourseReq;
import com.example.quanlytruonghoc.models.data.req.course.CourseStatusReq;
import com.example.quanlytruonghoc.models.data.res.CourseRes;
import com.example.quanlytruonghoc.models.data.mapper.CourseMapper;
import com.example.quanlytruonghoc.models.repositories.ICourseRepository;
import com.example.quanlytruonghoc.models.repositories.IUserRepository;
import com.example.quanlytruonghoc.models.services.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements ICourseService {
    private final ICourseRepository courseRepository;
    private final IUserRepository userRepository;
    private final CourseMapper courseMapper;

    @Override
    public List<CourseRes> findAllBySearch(String search) {
        if (search == null || search.toString().isEmpty()){
            return courseMapper.toResponseList(courseRepository.findAll());
        }
        return courseMapper.toResponseList(courseRepository.findAllBySearch(search));
    }

    @Override
    public CourseRes createCourse(CourseReq req) {
        User teacher = userRepository.findTeacherById(req.getTeacherId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy giảng viên hợp lệ với ID: " + req.getTeacherId()));
        Course course = courseMapper.toEntity(req);
        course.setTeacher(teacher);
        course.setStatus(CourseStatus.DRAFT);
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    public CourseRes updateCourseStatus(Long courseId, CourseStatusReq req) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy khóa học với ID: " + courseId));
        course.setStatus(req.getStatus());
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    public CourseRes updateCourse(Long id, CourseReq req) {
        Course update = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy khóa học với ID: " + id));
        User teacher = userRepository.findTeacherById(req.getTeacherId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy giảng viên hợp lệ với ID: " + req.getTeacherId()));
        courseMapper.updateEntity(req, update);
        update.setTeacher(teacher);
        update.setStatus(CourseStatus.DRAFT);
        return courseMapper.toResponse(courseRepository.save(update));
    }

    @Override
    public void deleteCourse(Long id) {
        Course deleteCourse = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy khóa học với ID: " + id));
        courseRepository.delete(deleteCourse);
    }

}