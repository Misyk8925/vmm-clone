package org.misha.student_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



public record GradeDto(
        Long id,
        Long version,
        Integer mark,
        String className,
        String subject,
        String teacherName,
        String studentName,
        String comment
) {}
