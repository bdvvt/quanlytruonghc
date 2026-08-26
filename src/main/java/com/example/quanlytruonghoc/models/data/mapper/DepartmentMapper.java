package com.example.quanlytruonghoc.models.data.mapper;

import com.example.quanlytruonghoc.exceptions.NotFoundException;
import com.example.quanlytruonghoc.models.data.entities.Department;
import com.example.quanlytruonghoc.models.data.entities.School;
import com.example.quanlytruonghoc.models.data.req.department.DepartmentReq;
import com.example.quanlytruonghoc.models.data.res.DepartmentRes;
import com.example.quanlytruonghoc.models.data.res.PageResponse;
import com.example.quanlytruonghoc.models.data.res.SchoolRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {UserMapper.class, SchoolMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DepartmentMapper {
    DepartmentRes toResponse(Department department);

    List<DepartmentRes> toResponseList(List<Department> departments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "school", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Department toEntity(DepartmentReq request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "school", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateEntity(DepartmentReq request, @MappingTarget Department department);

    default PageResponse<DepartmentRes> toPageResponse(Page<Department> page) {
        if (page == null) throw new NotFoundException("Không tìm thấy dữ liệu phòng ban");
        return PageResponse.<DepartmentRes>builder()
                .items(toResponseList(page.getContent()))
                .page(page.getNumber())
                .size(page.getSize())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
