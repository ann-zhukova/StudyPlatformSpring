package com.example.studyplatformspring.controller;


import com.example.studyplatformspring.entity.Task;
import com.example.studyplatformspring.entity.Topic;
import com.example.studyplatformspring.service.TaskService;
import com.example.studyplatformspring.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.List;

import com.example.studyplatformspring.entity.TaskStatus;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TopicService topicService;

    @GetMapping
    public String listTasks(Model model,
                            @RequestParam(required = false) String search,
                            @RequestParam(required = false) Long topicId,
                            @RequestParam(required = false) TaskStatus status) {
        List<Task> tasks;

        if (search != null && !search.trim().isEmpty()) {
            tasks = taskService.searchTasks(search);
            model.addAttribute("search", search);
        } else if (topicId != null && status != null) {
            tasks = taskService.getTasksByTopicAndStatus(topicId, status);
            model.addAttribute("selectedTopicId", topicId);
            model.addAttribute("selectedStatus", status);
            topicService.getTopicById(topicId).ifPresent(t -> model.addAttribute("selectedTopic", t));
        } else if (topicId != null) {
            tasks = taskService.getTasksByTopic(topicId);
            model.addAttribute("selectedTopicId", topicId);
            topicService.getTopicById(topicId).ifPresent(t -> model.addAttribute("selectedTopic", t));
        } else if (status != null) {
            tasks = taskService.getTasksByStatus(status);
            model.addAttribute("selectedStatus", status);
        } else {
            tasks = taskService.getAllTasks();
        }

        List<Topic> topics = topicService.getAllTopics();
        model.addAttribute("tasks", tasks);
        model.addAttribute("topics", topics);
        model.addAttribute("task", new Task());
        model.addAttribute("allStatuses", TaskStatus.values());
        return "tasks/list";
    }

    @GetMapping("/create")
    public String createTaskForm(Model model) {
        List<Topic> topics = topicService.getAllTopics();
        model.addAttribute("task", new Task());
        model.addAttribute("topics", topics);
        model.addAttribute("allStatuses", TaskStatus.values());
        return "tasks/form";
    }

    @PostMapping
    public String createTask(@ModelAttribute Task task,
                             @RequestParam Long topicId,
                             @RequestParam TaskStatus completionStatus) {
        Topic topic = topicService.getTopicById(topicId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid topic ID: " + topicId));
        task.setTopic(topic);
        task.setCompletionStatus(completionStatus);
        taskService.saveTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String editTaskForm(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + id));
        List<Topic> topics = topicService.getAllTopics();
        model.addAttribute("task", task);
        model.addAttribute("topics", topics);
        model.addAttribute("allStatuses", TaskStatus.values());
        return "tasks/form";
    }

    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable Long id,
                             @ModelAttribute Task task,
                             @RequestParam Long topicId,
                             @RequestParam TaskStatus completionStatus) {
        Topic topic = topicService.getTopicById(topicId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid topic ID: " + topicId));
        task.setId(id);
        task.setTopic(topic);
        task.setCompletionStatus(completionStatus);
        taskService.saveTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }

    @GetMapping("/status/{id}/{status}")
    public String updateTaskStatus(@PathVariable Long id, @PathVariable TaskStatus status) {
        taskService.updateTaskStatus(id, status);
        return "redirect:/tasks";
    }

    @GetMapping("/next-status/{id}")
    public String moveToNextStatus(@PathVariable Long id) {
        taskService.moveToNextStatus(id);
        return "redirect:/tasks";
    }
}
