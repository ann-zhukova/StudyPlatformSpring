package com.example.studyplatformspring.controller.api;

import com.example.studyplatformspring.dto.TasksResponse;
import com.example.studyplatformspring.entity.Task;
import com.example.studyplatformspring.entity.TaskStatus;
import com.example.studyplatformspring.entity.Topic;
import com.example.studyplatformspring.service.TaskService;
import com.example.studyplatformspring.service.TopicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/tasks", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class TaskRestController {

    private final TaskService taskService;
    private final TopicService topicService;

    public TaskRestController(TaskService taskService, TopicService topicService) {
        this.taskService = taskService;
        this.topicService = topicService;
    }

    @GetMapping
    public TasksResponse getAll(@RequestParam(required = false) TaskStatus status,
                                @RequestParam(required = false) Long topicId) {
        List<Task> tasks;
        if (status != null && topicId != null) {
            tasks = taskService.getTasksByTopicAndStatus(topicId, status);
        } else if (status != null) {
            tasks = taskService.getTasksByStatus(status);
        } else if (topicId != null) {
            tasks = taskService.getTasksByTopic(topicId);
        } else {
            tasks = taskService.getAllTasks();
        }
        return new TasksResponse(tasks);
    }

    @GetMapping("/{id}")
    public Task getOne(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<Task> create(@RequestBody Task task, @RequestParam Long topicId) {
        Topic topic = topicService.getTopicById(topicId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid topicId"));
        task.setTopic(topic);
        if (task.getCompletionStatus() == null) {
            task.setCompletionStatus(TaskStatus.NOT_STARTED);
        }
        Task saved = taskService.saveTask(task);
        return ResponseEntity.created(URI.create("/api/tasks/" + saved.getId())).body(saved);
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public Task update(@PathVariable Long id, @RequestBody Task task, @RequestParam Long topicId) {
        Task existing = taskService.getTaskById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        Topic topic = topicService.getTopicById(topicId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid topicId"));
        existing.setTitle(task.getTitle());
        existing.setDescription(task.getDescription());
        existing.setCompletionStatus(task.getCompletionStatus() != null ? task.getCompletionStatus() : existing.getCompletionStatus());
        existing.setTopic(topic);
        return taskService.saveTask(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Task existing = taskService.getTaskById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        taskService.deleteTask(existing.getId());
        return ResponseEntity.noContent().build();
    }
}

