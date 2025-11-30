package com.example.studyplatformspring.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "change_log")
public class ChangeLogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Column(name = "change_type", nullable = false)
    private String changeType;

    @Column(name = "change_time", nullable = false)
    private Instant changeTime;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    public ChangeLogEntry() {
    }

    public ChangeLogEntry(String entityType, Long entityId, String changeType, Instant changeTime, String details) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.changeType = changeType;
        this.changeTime = changeTime;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Instant getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Instant changeTime) {
        this.changeTime = changeTime;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

