package com.example.quanlytruonghoc.controllers.course;

import com.example.quanlytruonghoc.models.data.dto.wrapper.ApiResponse;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.req.course.CourseReq;
import com.example.quanlytruonghoc.models.data.req.course.CourseStatusReq;
import com.example.quanlytruonghoc.models.data.res.CourseRes;
import com.example.quanlytruonghoc.models.data.res.PageResponse;
import com.example.quanlytruonghoc.models.services.ICourseService;
import com.example.quanlytruonghoc.security.principal.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final ICourseService courseService;

    @GetMapping
    public ApiResponse<PageResponse<CourseRes>> findAll(
            @PageableDefault(
                    page = 0,
                    size = 5,
                    sort = "title",
                    direction = Sort.Direction.ASC
            ) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "search", required = false) String search) {
        User currentUser = userDetails.getUser();
        return ApiResponse.success(
                "Lấy khóa học thành công",
                courseService.findAllBySearch(currentUser,pageable,search)
        );
    }

    @PostMapping
    public ApiResponse<CourseRes> addNewCourse(@AuthenticationPrincipal CustomUserDetails userDetails,@Valid @ModelAttribute CourseReq req) {
        User currentUser = userDetails.getUser();
        return ApiResponse.created(
                        "Thêm khóa học thành công",
                        courseService.createCourse(currentUser,req)
        );
    }

    @PutMapping("/{courseId}/status")
    public ApiResponse<CourseRes> updateCourseStatus(@AuthenticationPrincipal CustomUserDetails userDetails,@PathVariable Long courseId, @Valid @ModelAttribute CourseStatusReq req) {
        User currentUser = userDetails.getUser();
        return ApiResponse.success(
                "Cập nhật trạng thái khóa học thành công",
                courseService.updateCourseStatus(currentUser,courseId, req)
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<CourseRes> updateCourse(@AuthenticationPrincipal CustomUserDetails userDetails,@PathVariable Long id, @Valid @ModelAttribute CourseReq req){
        User currentUser = userDetails.getUser();
        return ApiResponse.success(
                        "Cập nhật khóa học thành công",
                        courseService.updateCourse(currentUser,id, req)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> dropout(@AuthenticationPrincipal CustomUserDetails userDetails,@PathVariable Long id){
        User currentUser = userDetails.getUser();
        courseService.deleteCourse(currentUser,id);
        return ResponseEntity.noContent().build();
    }

}
