package com.example.quanlytruonghoc.models.services.impl;

import com.example.quanlytruonghoc.exceptions.NotFoundException;
import com.example.quanlytruonghoc.models.constants.CourseStatus;
import com.example.quanlytruonghoc.models.data.entities.Course;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.req.CourseReq;
import com.example.quanlytruonghoc.models.data.req.CourseStatusReq;
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

    @Override
    public List<Course> findAllBySearch(String search) {
        if (search == null || search.toString().isEmpty()){
            return courseRepository.findAll();
        }
        return courseRepository.findAllBySearch(search);
    }

    @Override
    public Course createCourse(CourseReq req) {
        User teacher = userRepository.findTeacherById(req.getTeacherId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy giảng viên hợp lệ với ID: " + req.getTeacherId()));
        Course course = Course.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .teacher(teacher)
                .price(req.getPrice())
                .durationHours(req.getDurationHours())
                .status(CourseStatus.DRAFT)
                .build();
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourseStatus(Long courseId, CourseStatusReq req) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy khóa học với ID: " + courseId));
        course.setStatus(req.getStatus());
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Long id, CourseReq req) {
        Course update = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy khóa học với ID: " + id));
        User teacher = userRepository.findTeacherById(req.getTeacherId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy giảng viên hợp lệ với ID: " + req.getTeacherId()));
        update.setTitle(req.getTitle());
        update.setDescription(req.getDescription());
        update.setTeacher(teacher);
        update.setPrice(req.getPrice());
        update.setDurationHours(req.getDurationHours());
        update.setStatus(CourseStatus.DRAFT);
        return courseRepository.save(update);
    }

    @Override
    public void deleteCourse(Long id) {
        Course deleteCourse = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy khóa học với ID: " + id));
        courseRepository.delete(deleteCourse);
    }

}
