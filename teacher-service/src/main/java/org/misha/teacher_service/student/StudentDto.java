package org.misha.teacher_service.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.misha.teacher_service.student.Student;


public record StudentDto(


        @NotBlank(message = "Name is mandatory")
        @Size(min = 2, max= 40, message = "Name must be between 2 and 40 characters")
        String name,

        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Password is mandatory")
        @Size(min = 4, max = 20, message = "Password must be between 4 and 20 characters")
        String password,

        @NotBlank(message = "Class name is mandatory")
        @Size(min = 2, max = 8, message = "Class name must be between 2 and 20 characters")
        String className) {
        public Student orElseThrow(Object studentNotFound) {
                return null;
        }
}