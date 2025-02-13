package org.misha.student_service.kafka.consumer;

import org.misha.student_service.dto.GradeDto;
import org.misha.student_service.model.Grade;
import org.misha.student_service.model.GradeMapper;
import org.misha.student_service.service.GradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@Service
public class GradeNotificationConsumer {

    private final Logger logger = LoggerFactory.getLogger(GradeNotificationConsumer.class);
    private final GradeService gradeService;
    private final GradeMapper gradeMapper;

    public GradeNotificationConsumer(GradeService gradeService, GradeMapper gradeMapper) {
        this.gradeService = gradeService;
        this.gradeMapper = gradeMapper;
    }

    @KafkaListener(
        topics = "grade-topic",
        containerFactory = "gradeKafkaListenerContainerFactory"
    )
    public void consume(GradeDto gradeDto) {
        try {
            logger.info("Starting to process grade DTO: {}", gradeDto);
            Grade grade = gradeMapper.toEntity(gradeDto);
            logger.info("Successfully mapped to entity: {}", grade);
            Grade savedGrade = gradeService.save(grade);
            logger.info("Successfully saved grade. New ID: {}, Grade details: {}", savedGrade.getId(), savedGrade);
        } catch (Exception e) {
            logger.error("Failed to process grade. DTO: {}, Error: {}", gradeDto, e.getMessage(), e);
            throw e;
        }
    }
}
