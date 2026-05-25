# Returning to an IT Development Career: A 9-Month Upskilling Plan

## About This Repository

I have 10+ years of Java development experience and 8+ years of IT Project Management experience. I am currently executing a systematic 9-month upskilling plan with the goal of re-entering the UK IT market as a **Senior Java Developer / Technical Lead**.

**Technical Focus:** Java 21 · Spring Boot 3 · Azure · AI Integration

**Target Market:** United Kingdom (UK)

**Plan Start Date:** 2026/04/12

***

## Learning Roadmap

| Phase | Timeline | Key Focus |
|-------|----------|-----------|
| Phase 1 | Weeks 1–8 | Reactivate technical foundations; establish AI-assisted development workflow |
| Phase 2 | Weeks 9–20 | Cloud technology (Azure) + AI integration development + Portfolio Project |
| Phase 3 | Weeks 21–28 | Specialist deepening + positioning establishment |
| Phase 4 | Weeks 29–36 | Job search preparation + active applications |

***

## Tech Stack

**Languages & Frameworks**
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green)

**Cloud & DevOps**
![Azure](https://img.shields.io/badge/Azure-AZ--900-blue)
![Docker](https://img.shields.io/badge/Docker-Containerization-blue)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-CI%2FCD-black)

**Development Tools**
![Cursor](https://img.shields.io/badge/Cursor-AI_IDE-purple)

***

## Learning Progress Log

### Week 1 (2026/05/13) — Environment Setup + Java 8–21 Syntax Fundamentals

**Completed:**

- ✅ Set up complete development environment (Java 21, Cursor IDE, Maven)
- ✅ Created `java-fundamentals` Maven project
- ✅ First hands-on experience with Lambda and Stream API through AI-assisted development
- ✅ Thoroughly read Baeldung articles: Java 8–21 New Features series
- ✅ Mastered: Lambda Expressions, Functional Interfaces, Optional, Stream API
- ✅ Mastered: Java 16 Records, Java 17 Sealed Classes, Pattern Matching

**This Week's Practice Project:**
- `java-fundamentals/` — Maven project containing all Week 1 practice code

**Biggest Takeaway This Week:**

***Lambda***: to define the logic/bahavior in a concise way without writing a whole named class or method and pass it as an argument to methods where a functional interface is expected. 

***Method Reference***: a shorthand way of referring an existing method, using `::` syntax and pass it as an argument to method instead of writing a lambda expression. It can refer to: 1) Static methods, 2) Instance methods of particular object, 3) Instance methods of arbitary object and 4) Constructor.

***Stream API***: use to prcoess a collection of data in a declarative way using chainable intermediate and terminal operations like `filter`, `map`, `reduce` and `collect`, enabling concise data-processing pipelines without explicitly writing loops.

***Optional***: a container that holds either a value or nothing, used to safely handle potentially absent values without using null.

***Functional Interfaces***: interfaces with only one single abstract method that can be implemented concisely using lambda expression or method references.

***Record***: immutable data carrier classes that all fields are final and the constructors, getters, `equal()`, `hashcode()` and `toString()` methods are automatically generated. They reduce the boilerplate for simple data objects.

***Sealed Classes***: restricts which other classes or interfaces may extend or implement them by declaring a fixed set of permitted subclasses. This enables more controlled inheritance and exhaustive pattern matching.

**Next Week's Plan:**
- Week 2: Deep dive into Java 17/21 new features; watch Amigoscode Java Functional Programming Full Course

***

## Learning Resources

| Type | Resource |
|------|----------|
| Articles | [Baeldung.com](https://www.baeldung.com) |
| Videos | [Amigoscode YouTube](https://www.youtube.com/@amigoscode) |
| AI Tools | [Cursor IDE](https://cursor.sh) |

***

### Week 2 ([Date]) — Deepening Modern Features of Java 17/21

**Completed Content:**
- ✅ Text Blocks: JSON / SQL / HTML templates, using `.formatted()`
- ✅ Records: DTO design pattern, compact constructor validation
- ✅ Sealed Classes: IT Ticket state flow type safety model
- ✅ Advanced Stream Collectors: `groupingBy` / `partitioningBy` / `toMap`
- ✅ Switch Expressions: Complete evolution of Java 14–21, Guarded Patterns
- ✅ Comprehensive refactoring of Java 8→21: Six refactoring points of LegacyHRSystem

**This Week's Highlight:**
Tell, Don’t Ask principle: can implement with enum or sealed class

**Next Week's Plan:**
Week 3: The basic of Spring Boot 3.x — REST API