package org.misha.student_service.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.misha.student_service.dto.GradeDto;

@Mapper(componentModel = "spring")
public interface GradeMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "version", source = "version")
    @Mapping(target = "mark", source = "mark")
    @Mapping(target = "className", source = "className")
    @Mapping(target = "subject", source = "subject")
    @Mapping(target = "teacherName", source = "teacherName")
    @Mapping(target = "studentName", source = "studentName")
    @Mapping(target = "comment", source = "comment")
    Grade toEntity(GradeDto dto);

    GradeDto toDto(Grade entity);
}
