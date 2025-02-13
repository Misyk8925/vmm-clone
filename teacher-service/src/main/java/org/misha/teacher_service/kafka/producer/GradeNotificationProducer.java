package org.misha.teacher_service.kafka.producer;


import org.springframework.messaging.Message;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service

public class GradeNotificationProducer {

    private final KafkaTemplate<String, GradeDto> kafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(GradeNotificationProducer.class);

    public GradeNotificationProducer(KafkaTemplate<String, GradeDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(GradeDto request) {
        logger.info("\n"+"Sending notification with body = < {} >", request);

        Message<GradeDto> message = MessageBuilder
                .withPayload(request)
                .setHeader(TOPIC, "grade-topic")
                .build();

        kafkaTemplate.send(message);
    }
}
