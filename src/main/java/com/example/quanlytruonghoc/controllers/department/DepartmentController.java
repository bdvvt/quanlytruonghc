package com.example.quanlytruonghoc.controllers.department;

import com.example.quanlytruonghoc.models.data.dto.wrapper.ApiResponse;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.req.department.DepartmentReq;
import com.example.quanlytruonghoc.models.data.req.school.SchoolReq;
import com.example.quanlytruonghoc.models.data.res.CourseRes;
import com.example.quanlytruonghoc.models.data.res.DepartmentRes;
import com.example.quanlytruonghoc.models.data.res.PageResponse;
import com.example.quanlytruonghoc.models.data.res.SchoolRes;
import com.example.quanlytruonghoc.models.services.IDepartmentService;
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
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final IDepartmentService departmentService;

    @GetMapping
    public ApiResponse<PageResponse<DepartmentRes>> findAll(
            @PageableDefault(
                    page = 0,
                    size = 5,
                    sort = "title",
                    direction = Sort.Direction.ASC
            ) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User currentUser = userDetails.getUser();
        return ApiResponse.success(
                "Lấy phòng ban thành công",
                departmentService.findAll(currentUser,pageable)
        );
    }

    @PostMapping
    public ApiResponse<DepartmentRes> addNewDepartment(@AuthenticationPrincipal CustomUserDetails userDetails, DepartmentReq req) {
        User currentUser = userDetails.getUser();
        return ApiResponse.created(
                "Thêm phòng ban thành công",
                departmentService.createDepartment(currentUser,req)
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<DepartmentRes> updateDepartment(@AuthenticationPrincipal CustomUserDetails userDetails,@PathVariable Long id, @Valid @ModelAttribute DepartmentReq req) {
        User currentUser = userDetails.getUser();
        return ApiResponse.success(
                "Cập nhật phòng ban thành công",
                departmentService.updateDepartment(currentUser,id, req)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> dropout(@AuthenticationPrincipal CustomUserDetails userDetails,@PathVariable Long id) {
        User currentUser = userDetails.getUser();
        departmentService.deleteDepartment(currentUser,id);
        return ResponseEntity.noContent().build();
    }

}
