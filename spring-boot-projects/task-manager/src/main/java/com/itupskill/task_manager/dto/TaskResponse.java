package com.itupskill.task_manager.dto;

import java.time.LocalDateTime;

import com.itupskill.task_manager.model.Task;

/**
 * TaskResponse - DTO returned to API clients.
 * 
 * Separating the response DTO from the internal Task model is intentional: 
 * if the internal model changes, the API contract (what clients see) does not
 * have to change
 */
public record TaskResponse(
        int id,
        String title,
        String description,
        String status,
        String priority,
        LocalDateTime createdAt) {

    /**
     * Factory method: converts internal Task model to TaskResponse DTO.
     * Deliberately excludes updatedAt from the API response.
     */
    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.id(),
                task.title(),
                task.description(),
                task.status(),
                task.priority(),
                task.createdAt());
    }
}
