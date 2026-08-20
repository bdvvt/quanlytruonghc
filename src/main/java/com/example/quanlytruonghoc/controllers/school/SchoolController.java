package com.example.quanlytruonghoc.controllers.school;

import com.example.quanlytruonghoc.models.data.dto.wrapper.ApiResponse;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.req.course.CourseReq;
import com.example.quanlytruonghoc.models.data.req.course.CourseStatusReq;
import com.example.quanlytruonghoc.models.data.req.school.SchoolReq;
import com.example.quanlytruonghoc.models.data.res.CourseRes;
import com.example.quanlytruonghoc.models.data.res.SchoolRes;
import com.example.quanlytruonghoc.models.services.ISchoolService;
import com.example.quanlytruonghoc.security.principal.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schools")
@RequiredArgsConstructor
public class SchoolController {
    private final ISchoolService schoolService;

    @PostMapping
    public ApiResponse<SchoolRes> addNewSchool(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @ModelAttribute SchoolReq req) {
        User currentUser = userDetails.getUser();
        return ApiResponse.created(
                "Thêm khóa học thành công",
                schoolService.createSchool(currentUser,req)
        );
    }
    @PutMapping("/{schoolId}")
    public ApiResponse<SchoolRes> updateCourseStatus(@AuthenticationPrincipal CustomUserDetails userDetails,@PathVariable Long schoolId, @Valid @ModelAttribute SchoolReq req) {
        User currentUser = userDetails.getUser();
        return ApiResponse.success(
                "Cập nhật trạng thái khóa học thành công",
                schoolService.updateSchool(currentUser,schoolId, req)
        );
    }

}
