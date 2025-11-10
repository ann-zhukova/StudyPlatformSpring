package com.example.studyplatformspring.repository;

import com.example.studyplatformspring.entity.Task;
import com.example.studyplatformspring.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCompletionStatus(TaskStatus completionStatus);

    List<Task> findByTopic_Id(Long topicId);

    @Query("SELECT t FROM Task t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Task> findByTitleOrDescriptionContaining(@Param("keyword") String keyword);

    List<Task> findByTopic_IdAndCompletionStatus(Long topicId, TaskStatus status);

    long countByCompletionStatus(TaskStatus status);
}
