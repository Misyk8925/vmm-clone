package org.misha.teacher_service.kafka.producer;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Version
    private Long version;

    // grade in austrian school system are from 1 to 5
    @Column(name = "mark", nullable = false)
    private Integer mark;

    @Column(name = "class_number", nullable = false)
    private String className;

    @Column(name = "subject", nullable = false)
    private String subject;


    @Column(name = "teacher_name", nullable = false)
    private String teacherName;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "comment")
    private String comment;
}