package com.example.studyplatformspring.messaging;

import com.example.studyplatformspring.entity.Task;
import com.example.studyplatformspring.entity.Topic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class EntityChangePublisher {

    private final JmsTemplate jmsTemplate;
    private final String destination;

    public EntityChangePublisher(JmsTemplate jmsTemplate,
                                 @Value("${app.jms.entity-change-destination}") String destination) {
        this.jmsTemplate = jmsTemplate;
        this.destination = destination;
    }

    public void publishTaskChange(String changeType, Task task, String details) {
        Long id = task != null ? task.getId() : null;
        EntityChangeEvent event = new EntityChangeEvent(
                "Task",
                id,
                changeType,
                Instant.now(),
                details
        );
        jmsTemplate.convertAndSend(destination, event);
    }

    public void publishTopicChange(String changeType, Topic topic, String details) {
        Long id = topic != null ? topic.getId() : null;
        EntityChangeEvent event = new EntityChangeEvent(
                "Topic",
                id,
                changeType,
                Instant.now(),
                details
        );
        jmsTemplate.convertAndSend(destination, event);
    }
}

