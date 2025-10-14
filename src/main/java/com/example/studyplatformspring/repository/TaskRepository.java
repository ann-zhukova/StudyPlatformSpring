package com.example.studyplatformspring.repository;

import com.example.studyplatformspring.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.example.studyplatformspring.entity.TaskStatus;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCompletionStatus(TaskStatus completionStatus);

    List<Task> findByTopicId(Long topicId);

    @Query("SELECT t FROM Task t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Task> findByTitleOrDescriptionContaining(String keyword);

    List<Task> findByTopicIdAndCompletionStatus(Long topicId, TaskStatus status);

    long countByCompletionStatus(TaskStatus status);
}