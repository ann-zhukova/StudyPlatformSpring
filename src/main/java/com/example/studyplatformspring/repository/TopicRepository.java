package com.example.studyplatformspring.repository;

import com.example.studyplatformspring.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    @Query("SELECT t FROM Topic t ORDER BY t.estimatedHours DESC")
    List<Topic> findAllOrderByCompletionTimeDesc();
}
