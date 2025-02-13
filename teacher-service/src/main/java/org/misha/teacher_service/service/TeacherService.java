package org.misha.teacher_service.service;


import jakarta.validation.Valid;
import org.misha.teacher_service.dto.TeacherDto;
import org.misha.teacher_service.kafka.producer.GradeDto;
import org.misha.teacher_service.model.Teacher;
import org.misha.teacher_service.model.TeacherMapper;
import org.misha.teacher_service.repository.TeacherRepository;
import org.misha.teacher_service.student.Student;
import org.misha.teacher_service.student.StudentFilter;
import org.misha.teacher_service.student.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@Service
public class TeacherService{

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    private final TeacherMapper teacherMapper;
    private final Logger logger = LoggerFactory.getLogger(TeacherService.class);
    private final GradesService gradesService;

    public TeacherService(TeacherRepository teacherRepository, StudentRepository studentRepository, TeacherMapper teacherMapper, GradesService gradesService) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.teacherMapper = teacherMapper;
        this.gradesService = gradesService;
    }

    public Teacher create(TeacherDto teacher) {
        if (teacherRepository.existsByName(teacher.name())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Teacher with name '%s' already exists".formatted(teacher.name()));
        }
        if (teacherRepository.existsByEmail(teacher.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Teacher with email '%s' already exists".formatted(teacher.email()));
        }
        return teacherRepository.save(teacherMapper.toEntity(teacher));
    }

    public Page<Teacher> getAllPages(Pageable pageable) {
        return teacherRepository.findAll(pageable);
    }

    public Teacher getOne(Long id) {
        Optional<Teacher> teacherOptional = teacherRepository.findById(id);
        return teacherOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    public Teacher update(Long id, TeacherDto teacher) {
        if (!teacherRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id));
        }
        logger.info("Updating teacher with id: {}", id);
        logger.info(teacher.toString());
        Teacher del = delete(id);
        return teacherRepository.save(teacherMapper.toEntity(teacher));
    }

    public Teacher delete(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElse(null);
        if (teacher != null) {
            teacherRepository.delete(teacher);
        }
        return teacher;
    }

    public Teacher defaultUpdate(Long id, Teacher teacher) {
        if (!teacherRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id));
        }
        return teacherRepository.save(teacher);
    }



    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }

    public List<Student> getStudents(String className) {

        StudentFilter filter = new StudentFilter(className);
        return studentRepository.findAll(filter.toSpecification());
    }

    public void sendGrade(@Valid GradeDto grade) {
        logger.info("Sending grade: {}", grade);

        gradesService.createGrade(grade);
        logger.info("Grade sent successfully");
    }


    public Teacher getTeacherByName(String name) {
        return teacherRepository.findByName(name);
    }

    public boolean existsByName(String name) {
        return teacherRepository.existsByName(name);
    }
}
