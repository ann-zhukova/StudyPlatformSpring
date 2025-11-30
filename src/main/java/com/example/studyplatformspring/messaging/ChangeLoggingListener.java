package com.example.studyplatformspring.messaging;

import com.example.studyplatformspring.entity.ChangeLogEntry;
import com.example.studyplatformspring.repository.ChangeLogRepository;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ChangeLoggingListener {

    private final ChangeLogRepository changeLogRepository;

    public ChangeLoggingListener(ChangeLogRepository changeLogRepository) {
        this.changeLogRepository = changeLogRepository;
    }

    @JmsListener(destination = "${app.jms.entity-change-destination}")
    public void onEntityChange(EntityChangeEvent event) {
        ChangeLogEntry entry = new ChangeLogEntry(
                event.getEntityType(),
                event.getEntityId(),
                event.getChangeType(),
                event.getTimestamp(),
                event.getDetails()
        );
        changeLogRepository.save(entry);
    }
}

