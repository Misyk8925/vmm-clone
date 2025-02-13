package org.misha.teacher_service.model;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.misha.teacher_service.dto.TeacherDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TeacherMapper {
    Teacher toEntity(TeacherDto teacherDto);

    void updateTeacherFromDto(TeacherDto teacherDto, @MappingTarget Teacher teacher);
    TeacherDto toTeacherDto(Teacher teacher);
}