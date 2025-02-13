package org.misha.teacher_service.kafka.producer;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
@Component
@Qualifier("teacherGradeMapper")
public interface GradeMapper {
    Grade toEntity(GradeDto gradeDto);
    GradeDto toGradeDto(Grade grade);
}
