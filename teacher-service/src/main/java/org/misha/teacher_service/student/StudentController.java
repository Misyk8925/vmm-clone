package org.misha.teacher_service.student;

import jakarta.validation.Valid;
import org.misha.teacher_service.student.StudentDto;
import org.misha.teacher_service.student.Student;
import org.misha.teacher_service.student.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping("/get-all")
    public List<Student> getAll() {
        return service.getAll();
    }

    @GetMapping("/get_student/{studentName}")
    public StudentDto getOne(@PathVariable String studentName) {
        return service.getOne(studentName);
    }

    @PostMapping("/create_student")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentDto create(@RequestBody @Valid StudentDto dto) {
        return service.create(dto);
    }

    @PutMapping("/update_student/{id}")
    public StudentDto update(@PathVariable Long id, @RequestBody @Valid StudentDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/delete_student/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public StudentDto delete(@PathVariable Long id) {
        return service.delete(id);
    }
}

