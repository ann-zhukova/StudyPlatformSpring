package com.example.studyplatformspring.messaging;

import java.io.Serializable;
import java.time.Instant;

public class EntityChangeEvent implements Serializable {

    private String entityType;
    private Long entityId;
    private String changeType;
    private Instant timestamp;
    private String details;

    public EntityChangeEvent() {
    }

    public EntityChangeEvent(String entityType, Long entityId, String changeType, Instant timestamp, String details) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.changeType = changeType;
        this.timestamp = timestamp;
        this.details = details;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

