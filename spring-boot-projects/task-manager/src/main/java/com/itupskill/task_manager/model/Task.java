package com.itupskill.task_manager.model;

import java.time.LocalDateTime;

/**
 * Task - internal domain model representing a helpdesk task
 * 
 * Using a Record here because Task data is immutable once created.
 * In Week 4, this will be replaced by a JPA @Entity class
 * (JPA entities cannot be Records as they require a no-arg constructor).
 */
public record Task(
        int id,
        String title,
        String description,
        String status,
        String priority,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
