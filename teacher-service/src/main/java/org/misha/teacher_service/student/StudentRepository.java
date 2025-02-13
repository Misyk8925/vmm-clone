package org.misha.teacher_service.student;

import org.misha.teacher_service.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor {

    Student findByName(String name);

    boolean existsByName(String name);

    boolean existsByEmail(String email);
}