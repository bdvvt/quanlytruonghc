package com.example.quanlytruonghoc.models.services.impl;

import com.example.quanlytruonghoc.exceptions.BadRequestException;
import com.example.quanlytruonghoc.exceptions.NotFoundException;
import com.example.quanlytruonghoc.models.data.dto.constants.CourseStatus;
import com.example.quanlytruonghoc.models.data.entities.Course;
import com.example.quanlytruonghoc.models.data.entities.Department;
import com.example.quanlytruonghoc.models.data.entities.School;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.req.course.CourseReq;
import com.example.quanlytruonghoc.models.data.req.course.CourseStatusReq;
import com.example.quanlytruonghoc.models.data.res.CourseRes;
import com.example.quanlytruonghoc.models.data.mapper.CourseMapper;
import com.example.quanlytruonghoc.models.data.res.PageResponse;
import com.example.quanlytruonghoc.models.repositories.ICourseRepository;
import com.example.quanlytruonghoc.models.repositories.IUserRepository;
import com.example.quanlytruonghoc.models.services.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements ICourseService {
    private final ICourseRepository courseRepository;
    private final IUserRepository userRepository;
    private final CourseMapper courseMapper;

    @Override
    public PageResponse<CourseRes> findAllBySearch(User currentUser,Pageable pageable, String search) {
        if (search == null || search.toString().isEmpty()){
            return courseMapper.toPageResponse(courseRepository.findAll(pageable));
        }
        return courseMapper.toPageResponse(courseRepository.findAllBySearch(search,pageable ));
    }

    @Override
    public CourseRes createCourse(User currentUser,CourseReq req) {
        User teacher = userRepository.findTeacherById(req.getTeacherId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy giảng viên hợp lệ với ID: " + req.getTeacherId()));
        School school = currentUser.getSchool();
        Department department = currentUser.getDepartment();
        if (school == null) {
            throw new BadRequestException("Người dùng chưa được gán vào trường");
        }
        if (department == null) {
            throw new BadRequestException("Người dùng chưa được gán vào phòng ban");
        }
        Course course = courseMapper.toEntity(req);
        course.setTeacher(teacher);
        course.setStatus(CourseStatus.DRAFT);
        course.setDepartment(department);
        course.setSchool(school);
        course.setCreatedBy(currentUser);
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    public CourseRes updateCourseStatus(User currentUser,Long courseId, CourseStatusReq req) {
        Course course = getCourseAndCheckPermission(currentUser, courseId);
        course.setStatus(req.getStatus());
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    public CourseRes updateCourse(User currentUser,Long id, CourseReq req) {
        Course course = getCourseAndCheckPermission(currentUser,id);
        User teacher = userRepository.findTeacherById(req.getTeacherId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy giảng viên hợp lệ với ID: " + req.getTeacherId()));
        courseMapper.updateEntity(req, course);
        course.setTeacher(teacher);
        course.setStatus(CourseStatus.DRAFT);
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    public void deleteCourse(User currentUser,Long id) {
        Course course = getCourseAndCheckPermission(currentUser, id);
        courseRepository.delete(course);
    }

    private Course getCourseAndCheckPermission(User currentUser, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy khóa học với ID: " + courseId));
        School userSchool = currentUser.getSchool();
        Department userDepartment = currentUser.getDepartment();

        if (userSchool == null) {
            throw new BadRequestException("Người dùng chưa được gán vào trường");
        }

        if (userDepartment == null) {
            throw new BadRequestException("Người dùng chưa được gán vào phòng ban");
        }

        if (!course.getSchool().getId().equals(userSchool.getId())) {
            throw new BadRequestException("Bạn không có quyền thao tác khóa học của trường khác");
        }

        if (!course.getDepartment().getId().equals(userDepartment.getId())) {
            throw new BadRequestException("Bạn không có quyền thao tác khóa học của phòng ban khác");
        }
        return course;
    }

}