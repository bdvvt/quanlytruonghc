package com.example.quanlytruonghoc.models.data.mapper;

import com.example.quanlytruonghoc.models.data.entities.Course;
import com.example.quanlytruonghoc.models.data.entities.School;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.req.course.CourseReq;
import com.example.quanlytruonghoc.models.data.req.school.SchoolReq;
import com.example.quanlytruonghoc.models.data.res.CourseRes;
import com.example.quanlytruonghoc.models.data.res.PageResponse;
import com.example.quanlytruonghoc.models.data.res.SchoolRes;
import com.example.quanlytruonghoc.models.data.res.UserRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = UserMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SchoolMapper {
    SchoolRes toResponse(School school);

    List<SchoolRes> toResponseList(List<School> schools);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "departments", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    School toEntity(SchoolReq request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "departments", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateEntity(SchoolReq request, @MappingTarget School school);

    default PageResponse<SchoolRes> toPageResponse(Page<School> page) {
        if (page == null) return null;
        return PageResponse.<SchoolRes>builder()
                .items(toResponseList(page.getContent()))
                .page(page.getNumber())
                .size(page.getSize())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
