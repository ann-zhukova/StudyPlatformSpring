package com.example.studyplatformspring.service;


import com.example.studyplatformspring.entity.Task;
import com.example.studyplatformspring.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.example.studyplatformspring.entity.TaskStatus;


@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByCompletionStatus(status);
    }

    public List<Task> getTasksByTopic(Long topicId) {
        return taskRepository.findByTopicId(topicId);
    }

    public List<Task> searchTasks(String keyword) {
        return taskRepository.findByTitleOrDescriptionContaining(keyword);
    }

    public List<Task> getTasksByTopicAndStatus(Long topicId, TaskStatus status) {
        return taskRepository.findByTopicIdAndCompletionStatus(topicId, status);
    }

    public Task updateTaskStatus(Long id, TaskStatus newStatus) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setCompletionStatus(newStatus);
            return taskRepository.save(task);
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
            return taskRepository.save(task);
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
}