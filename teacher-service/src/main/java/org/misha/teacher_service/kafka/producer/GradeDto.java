package org.misha.teacher_service.kafka.producer;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * DTO for {@link Grade}
 * Represents a grade in the Austrian school system (1-5 scale)
 */
public record GradeDto(
        Long id,
        
        Long version,

        @NotNull(message = "Grade mark is required")
        @Min(value = 1, message = "Grade must be between 1 and 5")
        @Max(value = 5, message = "Grade must be between 1 and 5")
        Integer mark,

        @NotBlank(message = "Class name is required")
        @Length(min = 2, max = 8, message = "Class name must be between 2 and 8 characters")
        String className,

        @NotBlank(message = "Subject is required")
        @Length(min = 2, max = 100, message = "Subject must be between 2 and 100 characters")
        String subject,

        @NotBlank(message = "Teacher name is required")
        @Length(min = 2, max = 40, message = "Teacher name must be between 2 and 40 characters")
        String teacherName,

        @NotBlank(message = "Student name is required")
        @Length(min = 2, max = 40, message = "Student name must be between 2 and 40 characters")
        String studentName,

        @Length(max = 255, message = "Comment must not exceed 255 characters")
        String comment
) {
}