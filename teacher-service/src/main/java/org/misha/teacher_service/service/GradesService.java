package org.misha.teacher_service.service;

import org.misha.teacher_service.kafka.producer.Grade;
import org.misha.teacher_service.kafka.producer.GradeNotificationProducer;
import org.misha.teacher_service.kafka.producer.GradeDto;
import org.misha.teacher_service.kafka.producer.GradeMapper;
import org.misha.teacher_service.repository.GradeRepository;
import org.misha.teacher_service.repository.TeacherRepository;
import org.misha.teacher_service.student.StudentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;

import java.util.Objects;

@Service
@Validated
public class GradesService {

    private final GradeRepository repository;
    private final GradeMapper mapper;
    private final GradeNotificationProducer notificationProducer;
    private final Logger logger = LoggerFactory.getLogger(GradesService.class);

    private final StudentService studentService;
    private final TeacherRepository teacherRepository;

    public GradesService(
            GradeRepository repository,
            GradeMapper mapper,
            GradeNotificationProducer notificationProducer,

            StudentService studentService, TeacherRepository teacherRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.notificationProducer = notificationProducer;

        this.studentService = studentService;
        this.teacherRepository = teacherRepository;
    }

    @Transactional
    public void createGrade(@Valid GradeDto gradeDto) {
        logger.info("Starting to create grade: {}", gradeDto);
        validateGrade(gradeDto);

        try {
            Grade grade = mapper.toEntity(gradeDto);
            Grade savedGrade = repository.save(grade);
            logger.info("Successfully saved grade with ID: {}", savedGrade.getId());
            
            notificationProducer.sendNotification(gradeDto);
            logger.info("Successfully sent grade notification for ID: {}", savedGrade.getId());
            
        } catch (DataIntegrityViolationException e) {
            logger.error("Database constraint violation while saving grade: {}", gradeDto, e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                "Unable to save grade due to data constraint violation", e);
        } catch (Exception e) {
            logger.error("Unexpected error while creating grade: {}", gradeDto, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Unable to process grade creation", e);
        }
    }

    private void validateGrade(GradeDto grade) {
        validateNotNull(grade);
        validateTeacher(grade.teacherName());
        validateStudent(grade.studentName(), grade.className());
        validateGradeMark(grade.mark());
        validateClassName(grade.className());
        validateSubject(grade.subject());
    }

    private void validateNotNull(GradeDto grade) {
        if (grade == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Grade cannot be null");
        }
    }

    private void validateTeacher(String teacherName) {
        if (isNullOrEmpty(teacherName) || !teacherRepository.existsByName(teacherName)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Teacher with name '%s' not found".formatted(teacherName)
            );
        }
    }

    private void validateStudent(String studentName, String className) {
        if (isNullOrEmpty(studentName) || !studentService.existsByName(studentName)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Student with name '%s' not found".formatted(studentName)
            );
        }

        var student = studentService.getOne(studentName);
        if (!Objects.equals(student.className(), className)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "Student '%s' is not in class '%s'".formatted(studentName, className)
            );
        }
    }

    private void validateGradeMark(Integer mark) {
        if (mark < 1 || mark > 5) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "Grade must be between 1 and 5"
            );
        }
    }

    private void validateClassName(String className) {
        if (className.length() < 2 || className.length() > 8) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "Class name must be between 2 and 8 characters"
            );
        }
    }

    private void validateSubject(String subject) {
        if (subject.length() < 2 || subject.length() > 100) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "Subject name must be between 2 and 100 characters"
            );
        }
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
