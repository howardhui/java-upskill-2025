package com.itupskill.task_manager.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.itupskill.task_manager.dto.CreateTaskRequest;
import com.itupskill.task_manager.dto.TaskResponse;
import com.itupskill.task_manager.dto.UpdateTaskRequest;
import com.itupskill.task_manager.model.Task;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TaskController - full CRUD REST API for task management.
 * 
 * HTTP method to operation mapping:
 *   GET    /api/v1/tasks        → retrieve all tasks
 *   GET    /api/v1/tasks/{id}   → retrieve one task
 *   POST   /api/v1/tasks        → create a new task
 *   PUT    /api/v1/tasks/{id}   → update an existing task
 *   DELETE /api/v1/takss/{id}   → delete a task
 * 
 * Storage: in-memory ArrayList (replaced by JPA in Week 4)
 */

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    // --- In-memory storage (temporary until Week 4 adds a real database) ---
    // ArrayList instead of List.of() so we can add/remove items
    private final List<Task> tasks = new ArrayList<>();

    // Thread-safe counter for generating unique IDs
    private final AtomicInteger idCounter = new AtomicInteger(1);

    // Pre-load some sample data when the controller is created
    public TaskController() {
        tasks.add(new Task(
                idCounter.getAndIncrement(),
                "Fix VPN connectivity issue",
                "Users in London office cannot connect to VPN",
                "IN_PROGRESS", "HIGH",
                LocalDateTime.now().minusHours(2),
                LocalDateTime.now().minusHours(2)));
        tasks.add(new Task(
                idCounter.getAndIncrement(),
                "Install quarterly security patches",
                "Apply security updates to all workstations",
                "OPEN", "MEDIUM",
                LocalDateTime.now().minusHours(5),
                LocalDateTime.now().minusHours(5)));
        tasks.add(new Task(
                idCounter.getAndIncrement(),
                "Replace broken keyboard",
                "Finance department user reported broken keyboard",
                "RESOLVED", "LOW",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusHours(3)));
    }

    // =========================================================
    // READ operations
    // =========================================================
    /**
     * GET /api/v1/taks
     * Returns all tasks.
     * HTTP 200 OK - even if the list is empty (empty array, not 404)
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<TaskResponse> response = tasks.stream()
                .map(TaskResponse::from)
                .toList(); // Java 16+ toList() → immutable
        return ResponseEntity.ok(response); // HTTP 200 OK
    }

    /**
     * GET /api/v1/task {id}
     * Returns a single task by ID.
     * HTTP 200 OK if found, HTTP 404 NOT Found if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskBy(@PathVariable int id) {
        return tasks.stream()
                .filter(task -> task.id() == id)
                .findFirst()
                .map(TaskResponse::from)
                .map(ResponseEntity::ok) // 200 OK
                .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }

    // =========================================================
    // CREATE operation
    // =========================================================
    /**
     * POST /api/v1/tasks
     * Create a new task from the request body.
     * HTTP 201 Created - signals that a new resource was created.
     * 
     * @RequestBody tells Spring to deserialise the JSON body
     *              into a CreateTaskRequest object automatically.
     */
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @RequestBody CreateTaskRequest request) {

        // build the new Task (server assigns id, timestamps, default status)
        Task newTask = new Task(
                idCounter.getAndIncrement(),
                request.title(),
                request.description(),
                "OPEN", // all new tasks start as OPEN
                request.priority(),
                LocalDateTime.now(),
                LocalDateTime.now());

        tasks.add(newTask);

        // 201 Created with the newly created task in the response body
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TaskResponse.from(newTask));
    }

    // =========================================================
    // UPDATE operation
    // =========================================================
    /**
     * PUT /api/v1/tasks/{id}
     * Replaces an existing task with new data.
     * HTTP 200 OK if updated, HTTP 404 Not Found if tasks does not exist.
     * 
     * Note: This is a full replacement (PUT), not a partial update (PATCH).
     * The client must send all updatable fields.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable int id,
            @RequestBody UpdateTaskRequest request) {

        // Find the index of the task to update
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).id() == id) {
                // Records are immutable - create a new Task with updated values
                Task updated = new Task(
                        id,
                        request.title(),
                        request.description(),
                        request.status(),
                        request.priority(),
                        tasks.get(i).createdAt(), // preserve original createdAt
                        LocalDateTime.now() // update the updateAt
                );
                tasks.set(i, updated);
                return ResponseEntity.ok(TaskResponse.from(updated)); // 200 OK
            }
        }
        return ResponseEntity.notFound().build(); // 404 Not Found
    }

    // =========================================================
    // DELETE operation
    // =========================================================
    /**
     * DELETE /api/v1/tasks/{id}
     * Deletes a task by ID
     * HTTP 204 No Content if deleted - success but no body to return.
     * HTTP 404 Not Found if the task does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable int id) {
        boolean removed = tasks.removeIf(task -> task.id() == id);

        return removed
                ? ResponseEntity.noContent().build() // 204 No Content
                : ResponseEntity.notFound().build(); // 404 Not Found
    }

    // =========================================================
    // Utility: count endpoint (bonus - useful for testing)
    // =========================================================
    /**
     * GET /api/v1/tasks/count
     * Returns the total number of tasks.
     */
    @GetMapping("/count")
    public ResponseEntity<Integer> getTaskCount() {
        return ResponseEntity.ok(tasks.size());
    }

}