package com.example.quanlytruonghoc.models.data.mapper;

import com.example.quanlytruonghoc.models.data.entities.Course;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.req.course.CourseReq;
import com.example.quanlytruonghoc.models.data.res.CourseRes;
import com.example.quanlytruonghoc.models.data.res.PageResponse;
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
public interface CourseMapper {
    CourseRes toResponse(Course course);

    List<CourseRes> toResponseList(List<Course> courses);

    @Mapping(target = "courseId", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "school", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Course toEntity(CourseReq request);

    @Mapping(target = "courseId", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "school", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(CourseReq request, @MappingTarget Course course);

    default PageResponse<CourseRes> toPageResponse(Page<Course> page) {
        if (page == null) return null;
        return PageResponse.<CourseRes>builder()
                .items(toResponseList(page.getContent()))
                .page(page.getNumber())
                .size(page.getSize())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}