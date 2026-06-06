package com.itupskill.task_manager.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * TaskController - demonstrates JSON response with Records as DTOs
 * 
 * This controller uses an in-memory list as a temporary data store. In Week 4,
 * this will be replaced with a real PostgreSQL database via Spring Data JPA.
 * 
 * Note: Using Java 21 Records as response DTOs.
 * Spring Boot's Jackson library automatically serialise Records to JSON.
 */

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
    // --- Response DTO ---
    // Using a Record here: immutable, concise, auto-serialised to JSON
    // Jackson (Spring Boot's default JSON library) handles Records natively
    record TaskResponse(
            int id,
            String title,
            String description,
            String status,
            String priority,
            LocalDateTime createdAt) {
    }

    // --- Temporary in-memory data (replaced by DB in Week 4) ---
    private final List<TaskResponse> tasks = List.of(
            new TaskResponse(
                    1,
                    "Fix VPN connectivity issue",
                    "Users in London office cannot connect to VPN",
                    "IN_PROGRESS",
                    "HIGH",
                    LocalDateTime.now().minusHours(2)),
            new TaskResponse(
                    2,
                    "Install software updates",
                    "Quarterly security patches for all workstations",
                    "OPEN",
                    "MEDIUM",
                    LocalDateTime.now().minusHours(5)),
            new TaskResponse(
                    3,
                    "Replace broken keyboard",
                    "User in Finance department reported broken keyboard",
                    "RESOLVED",
                    "LOW",
                    LocalDateTime.now().minusDays(1))

    );

    /**
     * GET /api/v1/tasks
     * Returns all tasks as a JSON array.
     */
    @GetMapping
    public List<TaskResponse> getAllTasks() {
        return tasks;
    }

    /**
     * GET /api/v1/tasks/{id}
     * Returns a single task by ID.
     * Returns null (HTTP 200 with null body) if not found for now.
     * Proper error handling will be added in Week 3 Wednesday.
     * 
     * @param id the task ID from the URL path
     */
    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable int id) {
        return tasks.stream()
                .filter(task -> task.id() == id)
                .findFirst()
                .orElse(null);
    }

}
