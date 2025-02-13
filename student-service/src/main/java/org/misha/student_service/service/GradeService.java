package org.misha.student_service.service;

import org.misha.student_service.model.Grade;
import org.misha.student_service.repository.GradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GradeService {
    private final Logger logger = LoggerFactory.getLogger(GradeService.class);
    private final GradeRepository gradeRepository;
    private static final int MAX_RETRIES = 3;

    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @Transactional
    public Grade save(Grade grade) {
        logger.info("Starting to save grade: {}", grade);
        
        if (grade.getId() == null) {
            logger.info("Saving new grade (no ID)");
            return gradeRepository.save(grade);
        }

        logger.info("Attempting to save grade with ID: {}", grade.getId());
        int attempts = 0;
        while (attempts < MAX_RETRIES) {
            try {
                logger.info("Attempt {} to save grade", attempts + 1);
                Grade existingGrade = gradeRepository.findById(grade.getId())
                    .orElseGet(() -> {
                        logger.info("No existing grade found with ID {}, treating as new", grade.getId());
                        grade.setId(null);
                        return grade;
                    });

                if (existingGrade == grade) {
                    logger.info("Saving new grade");
                    return gradeRepository.save(grade);
                }

                logger.info("Updating existing grade with ID: {}", existingGrade.getId());
                // Update existing grade's fields
                existingGrade.setMark(grade.getMark());
                existingGrade.setClassName(grade.getClassName());
                existingGrade.setSubject(grade.getSubject());
                existingGrade.setTeacherName(grade.getTeacherName());
                existingGrade.setStudentName(grade.getStudentName());
                existingGrade.setComment(grade.getComment());
                
                Grade savedGrade = gradeRepository.save(existingGrade);
                logger.info("Successfully saved grade: {}", savedGrade);
                return savedGrade;
            } catch (ObjectOptimisticLockingFailureException e) {
                attempts++;
                logger.warn("Optimistic locking failure on attempt {}. Exception: {}", attempts, e.getMessage());
                if (attempts == MAX_RETRIES) {
                    logger.error("Failed to save grade after {} attempts. Grade: {}", MAX_RETRIES, grade);
                    throw new ObjectOptimisticLockingFailureException("Failed to save grade after " + MAX_RETRIES + " attempts", e);
                }
                try {
                    long sleepTime = 100 * attempts;
                    logger.info("Waiting {} ms before retry", sleepTime);
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted during retry", ie);
                }
            } catch (Exception e) {
                logger.error("Unexpected error while saving grade: {}", e.getMessage(), e);
                throw e;
            }
        }
        
        String errorMsg = "Failed to save grade after " + MAX_RETRIES + " attempts";
        logger.error(errorMsg);
        throw new RuntimeException(errorMsg);
    }

}
