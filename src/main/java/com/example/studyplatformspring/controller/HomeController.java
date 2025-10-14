package com.example.studyplatformspring.controller;

import com.example.studyplatformspring.entity.TaskStatus;
import com.example.studyplatformspring.service.TaskService;
import com.example.studyplatformspring.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TopicService topicService;

    @GetMapping("/")
    public String home(Model model) {
        long totalTasks = taskService.getAllTasks().size();
        long notStartedTasks = taskService.getTaskCountByStatus(TaskStatus.NOT_STARTED);
        long inProgressTasks = taskService.getTaskCountByStatus(TaskStatus.IN_PROGRESS);
        long completedTasks = taskService.getTaskCountByStatus(TaskStatus.COMPLETED);
        long totalTopics = topicService.getAllTopics().size();

        model.addAttribute("totalTasks", totalTasks);
        model.addAttribute("notStartedTasks", notStartedTasks);
        model.addAttribute("inProgressTasks", inProgressTasks);
        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("totalTopics", totalTopics);

        return "home";
    }
}