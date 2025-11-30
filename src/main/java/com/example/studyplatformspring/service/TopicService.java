package com.example.studyplatformspring.service;

import com.example.studyplatformspring.entity.Topic;
import com.example.studyplatformspring.repository.TopicRepository;
import com.example.studyplatformspring.messaging.EntityChangePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private EntityChangePublisher entityChangePublisher;

    public List<Topic> getAllTopics() {
        return topicRepository.findAllOrderByCompletionTimeDesc();
    }

    public Optional<Topic> getTopicById(Long id) {
        return topicRepository.findById(id);
    }

    public Topic saveTopic(Topic topic) {
        boolean isNew = topic.getId() == null;
        Topic saved = topicRepository.save(topic);
        String changeType = isNew ? "INSERT" : "UPDATE";
        String details = buildTopicDetails(saved);
        entityChangePublisher.publishTopicChange(changeType, saved, details);
        return saved;
    }

    public void deleteTopic(Long id) {
        Optional<Topic> existing = topicRepository.findById(id);
        topicRepository.deleteById(id);
        existing.ifPresent(topic -> {
            String details = buildTopicDetails(topic);
            entityChangePublisher.publishTopicChange("DELETE", topic, details);
        });
    }

    public List<Topic> searchTopics(String keyword) {
        return topicRepository.searchByKeyword(keyword);
    }

    private String buildTopicDetails(Topic topic) {
        return "title=" + topic.getTitle()
                + ", description=" + topic.getDescription()
                + ", estimatedHours=" + topic.getEstimatedHours();
    }
}
