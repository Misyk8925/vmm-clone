package org.misha.teacher_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.misha.teacher_service.model.Teacher;

/**
 * DTO for {@link Teacher}
 */

public record TeacherDto(

        @NotBlank(message = "name is required") @Size(message = "password should include 3-40 characters", min = 3, max = 40)
        String name,

        @NotBlank(message = "passwort is required") @Size(message = "password should include 3-40 characters", min = 3, max = 40)
        String password,

        @NotBlank(message = "email is required") @Email(message = "invalid email format")
        String email) {

}