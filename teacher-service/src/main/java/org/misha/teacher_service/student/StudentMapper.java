package org.misha.teacher_service.student;


import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.misha.teacher_service.student.StudentDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    Student toEntity(StudentDto studentDto);

    StudentDto toStudentDto(Student student);

    Student updateWithNull(StudentDto studentDto, @MappingTarget Student student);

    List<String> toStudentNames(List<Student> students);

    default String map(Student student) {
        return student.getName(); // to avoid No implementation found for method map
    }
}
