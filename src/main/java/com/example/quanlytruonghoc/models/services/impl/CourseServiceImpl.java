package com.example.quanlytruonghoc.models.services.impl;

import com.example.quanlytruonghoc.exceptions.NotFoundException;
import com.example.quanlytruonghoc.models.constants.CourseStatus;
import com.example.quanlytruonghoc.models.data.entities.Course;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.repositories.ICourseRepository;
import com.example.quanlytruonghoc.models.repositories.IUserRepository;
import com.example.quanlytruonghoc.models.services.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements ICourseService {
    private final ICourseRepository courseRepository;
    private final IUserRepository userRepository;

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

}
