package com.example.studyplatformspring.repository;

import com.example.studyplatformspring.entity.ChangeLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeLogRepository extends JpaRepository<ChangeLogEntry, Long> {
}

