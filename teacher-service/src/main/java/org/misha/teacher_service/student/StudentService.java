package org.misha.teacher_service.student;

import java.util.List;

import org.misha.teacher_service.student.StudentFilter;


import org.misha.teacher_service.student.StudentDto;
import org.misha.teacher_service.student.Student;
import org.misha.teacher_service.student.StudentMapper;
import org.misha.teacher_service.student.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;



    public StudentService(StudentRepository studentRepository,
                          StudentMapper studentMapper) {

        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;


    }


    public List<Student> getAll() {
        return studentRepository.findAll();
    }


    public StudentDto getOne(String studentName) {

        try {
            Student student = studentRepository.findByName(studentName);
            return studentMapper.toStudentDto(student);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with name `%s` not found".formatted(studentName), e);
        }
    }
//        Optional<Student> studentOptional = studentRepository.findById(id);
//        return studentMapper.toStudentDto(studentOptional.orElseThrow(() ->
//             Status.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));



    public StudentDto create(StudentDto dto) {
        Student student = studentMapper.toEntity(dto);
        if (studentRepository.existsByName(student.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student with name '%s' already exists".formatted(student.getName()));
        }
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student with email '%s' already exists".formatted(student.getEmail()));
        }
        Student resultStudent = studentRepository.save(student);
        return studentMapper.toStudentDto(resultStudent);
    }

    public StudentDto update(Long id, StudentDto dto) {
        Student student = studentRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
        studentMapper.updateWithNull(dto, student);
        Student resultStudent = studentRepository.save(student);
        return studentMapper.toStudentDto(resultStudent);
    }

    public StudentDto delete(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
        studentRepository.delete(student);
        return studentMapper.toStudentDto(student);
    }

    public List<Student> findAllByClassName(String className) {
        StudentFilter filter = new StudentFilter(className);
        return studentRepository.findAll(filter.toSpecification());
    }

    public boolean existsByName(String name) {
        return studentRepository.existsByName(name);
    }
}
