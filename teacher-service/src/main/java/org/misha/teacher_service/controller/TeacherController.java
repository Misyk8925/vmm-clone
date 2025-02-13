package org.misha.teacher_service.controller;

import jakarta.validation.Valid;
import org.misha.teacher_service.dto.TeacherDto;
import org.misha.teacher_service.kafka.producer.GradeDto;
import org.misha.teacher_service.model.Teacher;
import org.misha.teacher_service.model.TeacherMapper;
import org.misha.teacher_service.service.TeacherService;
import org.misha.teacher_service.student.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    private final TeacherMapper teacherMapper;

    public TeacherController(TeacherService teacherService, TeacherMapper teacherMapper) {
        this.teacherService = teacherService;
        this.teacherMapper = teacherMapper;

    }


    @PostMapping("/create_teacher")
    @ResponseStatus(HttpStatus.CREATED)
    public Teacher create(@RequestBody @Valid TeacherDto teacher) {
        return teacherService.create(teacher);
    }

    @GetMapping("/all_pages")
    public PagedModel<Teacher> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<Teacher> teachers = teacherService.getAllPages(Pageable.ofSize(size).withPage(page));
        return new PagedModel<>(teachers);
    }



    @GetMapping("/get_teacher")
    public ResponseEntity<Teacher> getOne(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(teacherService.getOne(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/update_teacher/{id}")
    public ResponseEntity<TeacherDto> update(@PathVariable Long id, @RequestBody @Valid TeacherDto teacher) {
        try {
            return ResponseEntity.ok(teacherMapper.toTeacherDto(teacherService.update(id, teacher)));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Teacher> delete(@PathVariable Long id) {
        try {
            Teacher teacher = teacherService.delete(id);
            return teacher!=null
                    ? ResponseEntity.ok(teacher)
                    : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public Teacher update1(@PathVariable Long id, @RequestBody @Valid Teacher teacher) {
        return teacherService.defaultUpdate(id, teacher);
    }


    @GetMapping
    public List<Teacher> getAll1() {
        return teacherService.getAll();
    }

    @GetMapping("/get_students/{className}")
    public ResponseEntity<List<Student>> getStudents(@PathVariable String className) {
        try {
            return ResponseEntity.ok(teacherService.getStudents(className));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/send_grade")
    public ResponseEntity<String> sendGrade(@RequestBody @Valid GradeDto grade) {
        try {
            teacherService.sendGrade(grade);
            return ResponseEntity.ok("Grade sent successfully");
        } catch (ResponseStatusException e) {
            throw e;  // Let Spring handle the specific HTTP status
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Error processing grade: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test endpoint working");
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("TeacherController is working");
    }
}

