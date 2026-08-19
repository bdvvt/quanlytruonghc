package com.example.quanlytruonghoc.controllers.course;

import com.example.quanlytruonghoc.models.data.dto.wrapper.ApiResponse;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.req.course.CourseReq;
import com.example.quanlytruonghoc.models.data.req.course.CourseStatusReq;
import com.example.quanlytruonghoc.models.data.res.CourseRes;
import com.example.quanlytruonghoc.models.services.ICourseService;
import com.example.quanlytruonghoc.security.principal.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final ICourseService courseService;

    @GetMapping
    public ApiResponse<List<CourseRes>> findAll(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(value = "search", required = false) String search) {
        User currentUser = userDetails.getUser();
        return ApiResponse.success(
                "Lấy khóa học thành công",
                courseService.findAllBySearch(search)
        );
    }

    @PostMapping
    public ApiResponse<CourseRes> addNewCourse(@Valid @ModelAttribute CourseReq req) {
        return ApiResponse.created(
                        "Thêm khóa học thành công",
                        courseService.createCourse(req)
        );
    }

    @PutMapping("/{courseId}/status")
    public ApiResponse<CourseRes> updateCourseStatus(@PathVariable Long courseId, @Valid @ModelAttribute CourseStatusReq req) {
        return ApiResponse.success(
                "Cập nhật trạng thái khóa học thành công",
                courseService.updateCourseStatus(courseId, req)
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<CourseRes> updateCourse(@PathVariable Long id, @Valid @ModelAttribute CourseReq req){
        return ApiResponse.success(
                        "Cập nhật khóa học thành công",
                        courseService.updateCourse(id, req)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> dropout(@PathVariable Long id){
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

}
