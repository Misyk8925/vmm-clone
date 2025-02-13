package org.misha.teacher_service.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.misha.teacher_service.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Page<Teacher> findAll(Pageable pageable);

    boolean existsByName(@NotBlank(message = "name is required") @Size(message = "password should include 3-40 characters", min = 3, max = 40) String name);

    boolean existsByEmail(@NotBlank(message = "email is required") @Email(message = "invalid email format") String email);

    Teacher findByName(String name);
}