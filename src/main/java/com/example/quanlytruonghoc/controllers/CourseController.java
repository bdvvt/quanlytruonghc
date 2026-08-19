package com.example.quanlytruonghoc.controllers;

import com.example.quanlytruonghoc.models.constants.CourseStatus;
import com.example.quanlytruonghoc.models.data.dto.wrapper.ApiResponse;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.req.CourseReq;
import com.example.quanlytruonghoc.models.data.req.CourseStatusReq;
import com.example.quanlytruonghoc.models.services.ICourseService;
import com.example.quanlytruonghoc.security.principal.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final ICourseService courseService;

    @GetMapping
    public ResponseEntity<?> findAll(@AuthenticationPrincipal CustomUserDetails userDetails,@RequestParam(value = "search", required = false) String search) {
        User currentUser = userDetails.getUser();
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .message("Lấy khóa học thành công")
                            .code(200)
                            .data(courseService.findAllBySearch(search))
                            .build()
            );

    }

    @PostMapping
    public ResponseEntity<?> addNewCourse(@Valid @ModelAttribute CourseReq req) {
        return ResponseEntity.status(201).body(
                ApiResponse.builder()
                        .message("thêm khóa học thành công")
                        .code(201)
                        .data(courseService.createCourse(req))
                        .build()
        );
    }

    @PutMapping("/{courseId}/status")
    public ResponseEntity<?> updateCourseStatus(@PathVariable Long courseId, @Valid @ModelAttribute CourseStatusReq req) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .message("Cập nhật trạng thái khóa học thành công")
                        .code(200)
                        .data(courseService.updateCourseStatus(courseId, req))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @Valid @ModelAttribute CourseReq req){
        return ResponseEntity.status(200).body(
                ApiResponse.builder()
                        .message("Cập nhật khóa học thành công")
                        .code(200)
                        .data(courseService.updateCourse(id, req))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> dropout(@PathVariable Long id){
        courseService.deleteCourse(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                ApiResponse.builder()
                        .message("xóa khóa học thành công")
                        .code(204)
                        .data(null)
                        .build()
        );
    }

}
