package org.misha.teacher_service.configuration;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaGradeTopicConfig {

    @Bean
    public NewTopic gradeTopic() {
        return TopicBuilder
                .name("grade-topic")
                .partitions(3)
                .build();
    }
}
