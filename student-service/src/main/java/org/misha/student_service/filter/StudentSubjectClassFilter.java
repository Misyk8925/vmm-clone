package org.misha.student_service.filter;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.misha.student_service.model.Grade;

public record StudentSubjectClassFilter(
        String className,
        String subject
) {
    public Specification<Grade> toSpecification() {
        return Specification.where(classNameSpec())
                .and(subjectSpec());
    }

    private Specification<Grade> classNameSpec() {
        return (root, query, cb) -> StringUtils.hasText(className)
                ? cb.equal(
                cb.lower(root.get("className")),
                className.toLowerCase()
        )
                : null;
    }

    private Specification<Grade> subjectSpec() {
        return (root, query, cb) -> StringUtils.hasText(subject)
                ? cb.equal(
                cb.lower(root.get("subject")),
                subject.toLowerCase()
        )
                : null;
    }
}