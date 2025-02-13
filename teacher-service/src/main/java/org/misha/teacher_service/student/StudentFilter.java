package org.misha.teacher_service.student;

import org.misha.teacher_service.student.Student;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public record StudentFilter(String className) {
    public Specification<Student> toSpecification() {
        return Specification.where(classNameSpec());
    }

    private Specification<Student> classNameSpec() {
        return ((root, query, cb) ->StringUtils.hasText(className)  // Проверяем, есть ли текст в className
                ? cb.equal(                    // Если есть, создаем условие равенства
                cb.lower(root.get("className")),  // Преобразуем имя класса в нижний регистр
                className.toLowerCase()           // Преобразуем искомое значение в нижний регистр
        )
                : null ); // Если текста нет, возвращаем null
    }


    /*

    StudentFilter filter = new StudentFilter("11А");
    Specification<Student> spec = filter.toSpecification();

    В SQL это будет примерно:
    SELECT * FROM student WHERE LOWER(class_name) = '11а'

     */
}