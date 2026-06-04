# Week 3 Plan — Spring Boot 3.x Basics: REST API

## Week Goal
Build a fully functional REST API with Spring Boot 3,
understand the core annotations and project structure,
and be comfortable running and testing endpoints with Postman.

## Daily Plan

### Monday — Spring Boot Project Setup + First REST Controller
Focus: spring.io project generation, @RestController, @GetMapping
Baeldung: https://www.baeldung.com/spring-boot-start
Action: Create spring-boot-practice project, first GET /hello endpoint

### Tuesday — Full CRUD REST API
Focus: @PostMapping, @PutMapping, @DeleteMapping, @RequestBody, @PathVariable
Baeldung: https://www.baeldung.com/building-a-restful-web-service-with-spring-and-java-based-configuration
Action: Build complete Task Manager CRUD API (in-memory, no DB yet)

### Wednesday — Input Validation + Exception Handling
Focus: @Valid, @NotNull, @ControllerAdvice, @ExceptionHandler
Baeldung:
- https://www.baeldung.com/spring-boot-bean-validation
- https://www.baeldung.com/exception-handling-for-rest-with-spring
Action: Add validation and global error handling to Task Manager

### Thursday — Connect to PostgreSQL via Spring Data JPA
Focus: @Entity, @Repository, JpaRepository, application.properties
Baeldung: https://www.baeldung.com/the-persistence-layer-with-spring-data-jpa
Action: Replace in-memory storage with real PostgreSQL database

### Friday — Weekly Review + Swagger / OpenAPI Docs
Focus: Springdoc OpenAPI, @Operation, week consolidation
Baeldung: https://www.baeldung.com/spring-rest-openapi-documentation
Action: Add Swagger UI to Task Manager, test all endpoints

### Saturday — Amigoscode: Learn Spring Boot 3 (2-hour video)
Video: https://www.youtube.com/watch?v=Nv2DERaMx-4
Strategy: Watch with Cursor open, implement alongside video

### Sunday — Amigoscode: Spring Boot Tutorial [2023] (reference)
Video: https://www.youtube.com/watch?v=9SGDpanrc8U
Strategy: Watch selectively for concepts not covered in 2-hour version
+ GitHub update + Week 4 planning

## Tools needed
- Postman (download if not already installed)
- Docker Desktop (for PostgreSQL container on Thursday)
- spring.io (project generator)

## Install Postman now (before Week 3 starts)
https://www.postman.com/downloads/

## Week 3 Success Criteria
By end of Week 3 I should be able to:
[ ] Explain what @SpringBootApplication does
[ ] Build a CRUD REST API from scratch without looking up syntax
[ ] Add input validation with meaningful error messages
[ ] Connect Spring Boot to PostgreSQL
[ ] View and test all endpoints via Swagger UI
[ ] Explain the difference between @Controller and @RestController