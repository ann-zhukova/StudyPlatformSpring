package com.example.studyplatformspring.controller;

import com.example.studyplatformspring.entity.Topic;
import com.example.studyplatformspring.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping
    public String listTopics(Model model, @RequestParam(required = false) String search) {
        List<Topic> topics;
        if (search != null && !search.trim().isEmpty()) {
            topics = topicService.getAllTopics();
            model.addAttribute("search", search);
        } else {
            topics = topicService.getAllTopics();
        }
        model.addAttribute("topics", topics);
        model.addAttribute("topic", new Topic());
        return "topics/list";
    }

    @PostMapping
    public String createTopic(@ModelAttribute Topic topic) {
        topicService.saveTopic(topic);
        return "redirect:/topics";
    }

    @GetMapping("/create")
    public String createTopicForm(Model model) {
        model.addAttribute("topic", new Topic());
        return "topics/form";
    }

    @GetMapping("/edit/{id}")
    public String editTopicForm(@PathVariable Long id, Model model) {
        Topic topic = topicService.getTopicById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid topic ID: " + id));
        model.addAttribute("topic", topic);
        return "topics/form";
    }

    @PostMapping("/update/{id}")
    public String updateTopic(@PathVariable Long id, @ModelAttribute Topic topic) {
        topic.setId(id);
        topicService.saveTopic(topic);
        return "redirect:/topics";
    }

    @GetMapping("/delete/{id}")
    public String deleteTopic(@PathVariable Long id) {
        topicService.deleteTopic(id);
        return "redirect:/topics";
    }
}
