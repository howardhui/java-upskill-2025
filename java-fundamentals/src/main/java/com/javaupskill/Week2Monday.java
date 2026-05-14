package com.javaupskill;

import java.net.CacheRequest;
import java.util.Set;

public class Week2Monday {
    public static void main(String[] args) {
        System.out.println("=== Text Block Exericse ===");
        textBlocksExercise();
        System.out.println("\n=== Records In-depth Exercise ===");
        recordsExercise();
    }

    private static void textBlocksExercise() {
        // Monday Exercise 1: Text Blocks

        // Old text handling (Java 1.4)
        String oldJson = "{\n" +
                "    \"name\": \"Alice\" \n" +
                "    \"age\": 30,\n" +
                "    \"city\": \"London\"\n" +
                "}";
        System.out.println("Old text handling JSON: ");
        System.out.println(oldJson);

        // Text Blocks handling (Java 15+)
        // Start with 3 double quote symbols, line break, content and end with 3 double
        // quote symbols
        String newJson = """
                {
                    "name": "Alice",
                    "age": 30,
                    "city": "London"
                }
                """;

        System.out.println("Text Block JSON: ");
        System.out.println(newJson);

        // SQL Query (Most common usage of Text Blocks)
        String oldSql = "SELECT u.name, u.email, t.title " +
                "FROM users u " +
                "JOIN tickets t ON u.id = t.user_id " +
                "WHERE t.status = 'OPEN' " +
                "AND u.department = 'IT' " +
                "ORDER BY t.created_at DESC";

        String newSql = """
                SELECT u.name, u.email, t.title
                FROM users u
                JOIN tickets t ON u.id = t.user_id
                WHERE t.status = 'OPEN'
                AND u.department = 'IT'
                ORDER BY t.created_at DESC
                """;

        System.out.println("SQL Readability Comparsion: ");
        System.out.println("Old: " + oldSql);
        System.out.println("\nNew: " + newSql);

        // Dynamic Text Insertion (With String.formatted())
        String name = "Bob";
        int age = 25;
        String city = "Manchester";

        String profile = """
                {
                    "name": "%s",
                    "age": %d,
                    "city": "%s"
                }
                """.formatted(name, age, city);
        System.out.println("Dynamic JSON object: ");
        System.out.println(profile);

        // HTML template (For stage 2 Profolio Project)
        String html = """
                <html>
                    <body>
                        <h1>Welcome, %s!</h1>
                        <p>Your account is active.</p>
                    </body>
                </html>
                """.formatted(name);
        System.out.println("HTML template: ");
        System.out.println(html);
    }

    private static void recordsExercise() {
        // Monday Exercise 2: Records deepen
        // Demostrate the Request / Response design of REST API

        // CreateTicketRequest: Request object for user Ticket submission
        record CreateTicketRequest(
                String title,
                String description,
                String priority, // "LOW", "MEDIUM", "HIGH", "CRITICAL"
                String requesterEmail) {
            // compact constructor for verification
            CreateTicketRequest {
                if (title == null || title.isBlank()) {
                    throw new IllegalArgumentException("Title cannot be empty.");
                }
                if (!Set.of("LOW", "MEDIUM", "HIGH", "CRITICAL").contains(priority)) {
                    throw new IllegalArgumentException("Invalid priority: " + priority);
                }
                // Trim the fields
                title = title.trim();
                description = description == null ? "" : description.trim();

            }
        }

        // TicketResponse: API to return Ticket detail from client
        record TicketResponse(
                int id,
                String title,
                String priority,
                String status,
                String createdAt) {
        }

        // Demostrate Request and Response API
        CreateTicketRequest request = new CreateTicketRequest(
                "   Unable to login VPN   ", // leading and trailing spaces, trim through compact constructor
                "Error code 800 occurred while attempting to connect to the VPN.",
                "HIGH",
                "user@company.com");
        System.out.println("Request title (trimmed): " + request.title());
        System.out.println("Request toString: " + request);

        TicketResponse response = new TicketResponse(
                1001,
                request.title(),
                request.priority(),
                "OPEN",
                "2025-01-13T09:00:00");
        System.out.println("Response: " + response);

        record ApiResponse<T>(
                boolean success,
                String message,
                T data) {
            // Custom method: convert Record to JSON string format
            // Note: Real projects will use the Jackson library to handle this
            // automatically.
            // This is written manually to understand the underlying concepts.
            String toJson() {
                return """
                        {
                            "success": %b,
                            "message": "%s"
                            "data": "%s"
                        }
                        """.formatted(success, message, data);
            }
        }

        // Demostrate success response
        ApiResponse<String> successResp = new ApiResponse<>(
                true,
                "Ticket created successfully",
                "Ticket #1001");
        System.out.println("API success response: ");
        System.out.println(successResp.toJson());

        // Demostrate failure response
        ApiResponse<String> errorResp = new ApiResponse<>(
                false,
                "Verification failed: Title cannot be empty.",
                null);
        System.out.println("API failure response: ");
        System.out.println(errorResp.toJson());

        // Test equal() method of Records class
        ApiResponse<String> dup = new ApiResponse<>(
                true, "Ticket created successfully", "Ticket #1001");
        System.out.println("Equal content: " + successResp.equals(dup));
    }
}
