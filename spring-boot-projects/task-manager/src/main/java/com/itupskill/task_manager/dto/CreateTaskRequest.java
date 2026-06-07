package com.itupskill.task_manager.dto;

/**
 * CreateTaskRequest - DTO for POST /api/v1/tasks request body.
 * 
 * Using a Record: the incoming request data should be immutable once received.
 * Validation annotations will be added on Wednesday.
 * 
 * Fields intentionally do NOT include id, createdAt, updatedAt - those are 
 * generated server-side, not provided by the client.
 */
public record CreateTaskRequest(
        String title,
        String description,
        String priority) {
}
