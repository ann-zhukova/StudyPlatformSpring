package com.example.studyplatformspring.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JacksonXmlRootElement(localName = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus completionStatus = TaskStatus.NOT_STARTED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    @JsonBackReference("topic-tasks")
    private Topic topic;

    public Task() {}

    public Task(String title, String description, TaskStatus completionStatus, Topic topic) {
        this.title = title;
        this.description = description;
        this.completionStatus = completionStatus;
        this.topic = topic;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TaskStatus getCompletionStatus() { return completionStatus; }
    public void setCompletionStatus(TaskStatus completionStatus) {
        this.completionStatus = completionStatus;
    }

    public Topic getTopic() { return topic; }
    public void setTopic(Topic topic) { this.topic = topic; }

    @Transient
    @JsonProperty("topicId")
    @JacksonXmlProperty(localName = "topicId")
    public Long getTopicId() {
        return topic != null ? topic.getId() : null;
    }
}
