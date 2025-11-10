package com.example.studyplatformspring.controller.api;

import com.example.studyplatformspring.dto.TopicsResponse;
import com.example.studyplatformspring.entity.Topic;
import com.example.studyplatformspring.service.TopicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/topics", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class TopicRestController {

    private final TopicService topicService;

    public TopicRestController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping
    public TopicsResponse getAll() {
        List<Topic> topics = topicService.getAllTopics();
        return new TopicsResponse(topics);
    }

    @GetMapping("/{id}")
    public Topic getOne(@PathVariable Long id) {
        return topicService.getTopicById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<Topic> create(@RequestBody Topic topic) {
        Topic saved = topicService.saveTopic(topic);
        return ResponseEntity.created(URI.create("/api/topics/" + saved.getId())).body(saved);
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public Topic update(@PathVariable Long id, @RequestBody Topic topic) {
        Topic existing = topicService.getTopicById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));
        existing.setTitle(topic.getTitle());
        existing.setDescription(topic.getDescription());
        existing.setEstimatedHours(topic.getEstimatedHours());
        existing.setTasks(topic.getTasks());
        return topicService.saveTopic(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Topic existing = topicService.getTopicById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));
        topicService.deleteTopic(existing.getId());
        return ResponseEntity.noContent().build();
    }
}

