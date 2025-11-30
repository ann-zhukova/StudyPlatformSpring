package com.example.studyplatformspring.service;


import com.example.studyplatformspring.entity.Task;
import com.example.studyplatformspring.entity.TaskStatus;
import com.example.studyplatformspring.repository.TaskRepository;
import com.example.studyplatformspring.messaging.EntityChangePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EntityChangePublisher entityChangePublisher;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task saveTask(Task task) {
        boolean isNew = task.getId() == null;
        Task saved = taskRepository.save(task);
        String changeType = isNew ? "INSERT" : "UPDATE";
        String details = buildTaskDetails(saved);
        entityChangePublisher.publishTaskChange(changeType, saved, details);
        return saved;
    }

    public void deleteTask(Long id) {
        Optional<Task> existing = taskRepository.findById(id);
        taskRepository.deleteById(id);
        existing.ifPresent(task -> {
            String details = buildTaskDetails(task);
            entityChangePublisher.publishTaskChange("DELETE", task, details);
        });
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByCompletionStatus(status);
    }

    public List<Task> getTasksByTopic(Long topicId) {
        return taskRepository.findByTopic_Id(topicId);
    }

    public List<Task> searchTasks(String keyword) {
        return taskRepository.findByTitleOrDescriptionContaining(keyword);
    }

    public List<Task> getTasksByTopicAndStatus(Long topicId, TaskStatus status) {
        return taskRepository.findByTopic_IdAndCompletionStatus(topicId, status);
    }

    public Task updateTaskStatus(Long id, TaskStatus newStatus) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setCompletionStatus(newStatus);
            return saveTask(task);
        }
        return null;
    }

    public Task moveToNextStatus(Long id) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            TaskStatus currentStatus = task.getCompletionStatus();
            TaskStatus nextStatus = getNextStatus(currentStatus);
            task.setCompletionStatus(nextStatus);
            return saveTask(task);
        }
        return null;
    }

    private TaskStatus getNextStatus(TaskStatus currentStatus) {
        return switch (currentStatus) {
            case NOT_STARTED -> TaskStatus.IN_PROGRESS;
            case IN_PROGRESS -> TaskStatus.COMPLETED;
            case COMPLETED -> TaskStatus.COMPLETED; // Остается завершенной
        };
    }

    public long getTaskCountByStatus(TaskStatus status) {
        return taskRepository.countByCompletionStatus(status);
    }

    private String buildTaskDetails(Task task) {
        Long topicId = task.getTopic() != null ? task.getTopic().getId() : null;
        return "title=" + task.getTitle()
                + ", description=" + task.getDescription()
                + ", status=" + task.getCompletionStatus()
                + ", topicId=" + topicId;
    }
}
