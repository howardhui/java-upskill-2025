package com.itupskill.task_manager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloController - demonstrates the basic REST controller setup.
 * 
 * Key annotations:
 * @RestController = @Controller + @ResponseBody
 *   Marks this class as a web controller where every method returns data
 *   (JSON/text) directly, not a view name.
 * 
 * @RequestMapping("/api/v1/hello")
 *   All endpoints in this controller are prefixed with this path.
 */

@RestController
@RequestMapping("/api/v1/hello")
public class HelloController {

    /**
     * GET /api/v1/hello
     * Returns a plain text greeting.
     */
    @GetMapping
    public String hello() {
        return "Hello from Spring Boot 3!";
    }

    /**
     * GET /api/v1/hello/java
     * Demonstrates using a path suffix
     */
    @GetMapping("/java")
    public String helloJava() {
        return "Hello, Java 21 + Spring Boot 3!";
    }
}
