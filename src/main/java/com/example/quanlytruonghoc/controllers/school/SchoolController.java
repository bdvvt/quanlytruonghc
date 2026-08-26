package com.example.quanlytruonghoc.controllers.school;

import com.example.quanlytruonghoc.models.data.dto.wrapper.ApiResponse;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.req.course.CourseReq;
import com.example.quanlytruonghoc.models.data.req.course.CourseStatusReq;
import com.example.quanlytruonghoc.models.data.req.school.SchoolReq;
import com.example.quanlytruonghoc.models.data.res.CourseRes;
import com.example.quanlytruonghoc.models.data.res.PageResponse;
import com.example.quanlytruonghoc.models.data.res.SchoolRes;
import com.example.quanlytruonghoc.models.services.ISchoolService;
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
@RequestMapping("/api/schools")
@RequiredArgsConstructor
public class SchoolController {
    private final ISchoolService schoolService;

    @PostMapping
    public ApiResponse<SchoolRes> addNewSchool(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @ModelAttribute SchoolReq req) {
        User currentUser = userDetails.getUser();
        return ApiResponse.created(
                "Thêm trường học thành công",
                schoolService.createSchool(currentUser,req)
        );
    }

    @PutMapping("/{schoolId}")
    public ApiResponse<SchoolRes> updateSchool(@AuthenticationPrincipal CustomUserDetails userDetails,@PathVariable Long schoolId, @Valid @ModelAttribute SchoolReq req) {
        User currentUser = userDetails.getUser();
        return ApiResponse.success(
                "Cập nhật trường học thành công",
                schoolService.updateSchool(currentUser,schoolId, req)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> dropout(@AuthenticationPrincipal CustomUserDetails userDetails,@PathVariable Long id){
        User currentUser = userDetails.getUser();
        schoolService.deleteSchool(currentUser,id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ApiResponse<PageResponse<SchoolRes>> findAll(
            @PageableDefault(
                    page = 0,
                    size = 5,
                    sort = "title",
                    direction = Sort.Direction.ASC
            ) Pageable pageable) {
        return ApiResponse.success(
                "Lấy trường hcoj thành công",
                schoolService.findAll(pageable)
        );
    }

}
