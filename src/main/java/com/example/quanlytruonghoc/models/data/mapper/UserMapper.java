package com.example.quanlytruonghoc.models.data.mapper;

import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.req.auth.RegisterReq;
import com.example.quanlytruonghoc.models.data.req.user.UserReq;
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
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    UserRes toResponse(User user);

    List<UserRes> toResponseList(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "otpCode", ignore = true)
    @Mapping(target = "otpExpiration", ignore = true)
    User toEntity(UserReq request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "otpCode", ignore = true)
    @Mapping(target = "otpExpiration", ignore = true)
    User toEntity(RegisterReq request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "otpCode", ignore = true)
    @Mapping(target = "otpExpiration", ignore = true)
    void updateEntity(UserReq request, @MappingTarget User user);

    default PageResponse<UserRes> toPageResponse(Page<User> page) {
        if (page == null) return null;
        return PageResponse.<UserRes>builder()
                .items(toResponseList(page.getContent()))
                .page(page.getNumber())
                .size(page.getSize())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}