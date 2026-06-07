package com.itupskill.task_manager.dto;

/**
 * UpdateTaskRequest - DTO for PUT /api/v1/tasks/{id} request body.
 * 
 * Allows updating title, description, status, and priority.
 * The client provided what fields to update; server handles the updatedAt
 * timestamp automatically.
 */
public record UpdateTaskRequest(
        String title,
        String description,
        String status,
        String priority) {

}
