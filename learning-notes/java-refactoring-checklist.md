# Java 8 → Java 21 Refactoring Checklist

A practical reference for modernising legacy Java code. Each item follows the pattern: **trigger → solution → example → benefit**.

***

## ✅ 1. Anonymous Classes → Lambda Expressions

**Trigger:** You see `new SomeInterface() { ... }` where the interface has only one abstract method (a functional interface).

**Java 21 Feature:** Lambda expressions (Java 8+)

```java
// ❌ BEFORE: Anonymous class
Runnable r = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello!");
    }
};

// ✅ AFTER: Lambda
Runnable r = () -> System.out.println("Hello!");
```

**Benefit:** Dramatically reduces boilerplate. The intent is immediately clear — you're providing behaviour, not defining a type. Also works with `Comparator`, `Callable`, `Predicate`, etc.

***

## ✅ 2. Old-Style For-Loop → Stream API

**Trigger:** A `for` loop that filters, transforms, or accumulates elements into a new collection.

**Java 21 Feature:** Stream API with `filter()`, `map()`, `collect()` (Java 8+)

```java
// ❌ BEFORE: Imperative loop
List<String> result = new ArrayList<>();
for (String name : names) {
    if (name.startsWith("A")) {
        result.add(name.toUpperCase());
    }
}

// ✅ AFTER: Stream pipeline
List<String> result = names.stream()
    .filter(name -> name.startsWith("A"))
    .map(String::toUpperCase)
    .collect(Collectors.toList()); // or .toList() in Java 16+
```

**Benefit:** Declarative style — you describe *what* you want, not *how* to do it. Pipelines are easy to read, chain, and parallelise with `.parallelStream()`.

***

## ✅ 3. Null Checks → Optional

**Trigger:** Chains of `if (x != null)` guards, or methods that return `null` to signal "no value".

**Java 21 Feature:** `Optional<T>` (Java 8+)

```java
// ❌ BEFORE: Null checks everywhere
String city = null;
if (user != null && user.getAddress() != null) {
    city = user.getAddress().getCity();
}
String display = city != null ? city : "Unknown";

// ✅ AFTER: Optional chain
String display = Optional.ofNullable(user)
    .map(User::getAddress)
    .map(Address::getCity)
    .orElse("Unknown");
```

**Benefit:** Eliminates silent `NullPointerException` risks. The absence of a value is explicit and handled in a type-safe way. `orElse`, `orElseGet`, `orElseThrow`, and `ifPresent` cover all common scenarios cleanly.

***

## ✅ 4. Regular Class as Data Carrier → Record

**Trigger:** A class whose only purpose is to hold data — it has fields, a constructor, getters, `equals()`, `hashCode()`, and `toString()` all written manually.

**Java 21 Feature:** `record` (Java 16+, previewed in 14/15)

```java
// ❌ BEFORE: Verbose POJO
public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) { this.x = x; this.y = y; }
    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public boolean equals(Object o) { /* ... boilerplate ... */ }
    @Override
    public int hashCode() { /* ... boilerplate ... */ }
    @Override
    public String toString() { return "Point[x=" + x + ", y=" + y + "]"; }
}

// ✅ AFTER: Record (all of the above, generated automatically)
public record Point(int x, int y) {}

// Usage is the same:
Point p = new Point(3, 7);
System.out.println(p.x()); // accessor (no "get" prefix)
System.out.println(p);     // Point[x=3, y=7]
```

**Benefit:** Eliminates entire categories of boilerplate. Records are immutable by default, which makes them safe to share across threads. Ideal for DTOs, value objects, and map keys.

***

## ✅ 5. String Concatenation with `+` → Text Blocks

**Trigger:** Multi-line strings built with `+` and `\n`, especially for JSON, HTML, SQL, or any structured text.

**Java 21 Feature:** Text Blocks (Java 15+)

```java
// ❌ BEFORE: Unreadable string concatenation
String json = "{\n" +
              "  \"name\": \"Alice\",\n" +
              "  \"age\": 30\n" +
              "}";

// ✅ AFTER: Text block
String json = """
        {
          "name": "Alice",
          "age": 30
        }
        """;
```

**Benefit:** The string looks exactly like what it represents. No manual `\n` or escaped quotes. The compiler handles indentation stripping automatically based on the closing `"""` position.

***

## ✅ 6. Switch Statement with `break` → Switch Expression

**Trigger:** A `switch` statement where each `case` ends in `break`, often used to assign a value to a variable.

**Java 21 Feature:** Switch expressions with arrow syntax (Java 14+)

```java
// ❌ BEFORE: Switch statement prone to fall-through bugs
String label;
switch (day) {
    case MONDAY:
    case TUESDAY:
        label = "Early week";
        break;
    case FRIDAY:
        label = "End of week";
        break;
    default:
        label = "Midweek";
        break;
}

// ✅ AFTER: Switch expression — exhaustive, no fall-through, returns a value
String label = switch (day) {
    case MONDAY, TUESDAY -> "Early week";
    case FRIDAY          -> "End of week";
    default              -> "Midweek";
};
```

**Benefit:** Switch expressions must be exhaustive (compiler enforces it), eliminate accidental fall-through, and can be used directly as a value. Combine with sealed classes and pattern matching for powerful data dispatch.

***

## ✅ 7. `instanceof` + Cast → Pattern Matching for `instanceof`

**Trigger:** A block that does `if (x instanceof SomeType)` followed immediately by `(SomeType) x` to cast.

**Java 21 Feature:** Pattern matching for `instanceof` (Java 16+)

```java
// ❌ BEFORE: Redundant check and cast
if (shape instanceof Circle) {
    Circle c = (Circle) shape; // you already know it's a Circle!
    System.out.println("Radius: " + c.getRadius());
}

// ✅ AFTER: Pattern variable — check and bind in one step
if (shape instanceof Circle c) {
    System.out.println("Radius: " + c.getRadius());
}

// Even better in Java 21 with switch pattern matching:
String description = switch (shape) {
    case Circle c    -> "Circle with radius " + c.getRadius();
    case Rectangle r -> "Rectangle " + r.width() + "x" + r.height();
    default          -> "Unknown shape";
};
```

**Benefit:** Removes the redundant cast entirely. The pattern variable `c` is only in scope where the type is guaranteed, so it's both concise and safe.

***

## ✅ 8. Unconstrained Inheritance → Sealed Classes

**Trigger:** An abstract class or interface where only a known, fixed set of subclasses should ever exist, but there's nothing stopping anyone from adding more.

**Java 21 Feature:** Sealed classes and interfaces (Java 17+)

```java
// ❌ BEFORE: Anyone can subclass — no way to reason about all cases
public abstract class Shape { /* ... */ }
public class Circle extends Shape { /* ... */ }
public class Rectangle extends Shape { /* ... */ }
// Nothing stops: public class Hexagon extends Shape { } in any package

// ✅ AFTER: Sealed class — permitted subclasses are declared explicitly
public sealed class Shape permits Circle, Rectangle {}

public final class Circle    extends Shape { double radius; }
public final class Rectangle extends Shape { double width, height; }

// The compiler now knows all possible subtypes, enabling exhaustive switches:
double area = switch (shape) {
    case Circle c    -> Math.PI * c.radius * c.radius;
    case Rectangle r -> r.width * r.height;
    // No default needed — compiler knows these are all cases!
};
```

**Benefit:** Makes your type hierarchy a closed, known set. Compilers and tools can verify exhaustiveness. Combined with records and pattern matching, this is the foundation of algebraic data types in Java.

***

## ✅ 9. Manual Map Building → `groupingBy` / `toMap`

**Trigger:** A loop that populates a `Map` by checking `containsKey`, creating lists, and calling `put` manually.

**Java 21 Feature:** `Collectors.groupingBy()` and `Collectors.toMap()` (Java 8+)

```java
// ❌ BEFORE: Manual grouping loop
Map<String, List<Employee>> byDept = new HashMap<>();
for (Employee e : employees) {
    byDept.computeIfAbsent(e.getDepartment(), k -> new ArrayList<>()).add(e);
}

// ✅ AFTER: groupingBy collector
Map<String, List<Employee>> byDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment));

// ---

// ❌ BEFORE: Manual toMap loop
Map<Integer, String> idToName = new HashMap<>();
for (Employee e : employees) {
    idToName.put(e.getId(), e.getName());
}

// ✅ AFTER: toMap collector
Map<Integer, String> idToName = employees.stream()
    .collect(Collectors.toMap(Employee::getId, Employee::getName));
```

**Benefit:** Single-expression, declarative, and composable. `groupingBy` supports downstream collectors (e.g., counting, summing, mapping) for powerful multi-level aggregations without nested loops.

***

## ✅ 10. Comparator Anonymous Class → `Comparator.comparing()`

**Trigger:** A `Comparator` implemented as an anonymous class using `compare(a, b)` with manual field access.

**Java 21 Feature:** `Comparator.comparing()` with method references (Java 8+)

```java
// ❌ BEFORE: Anonymous Comparator
Collections.sort(employees, new Comparator<Employee>() {
    @Override
    public int compare(Employee a, Employee b) {
        return a.getLastName().compareTo(b.getLastName());
    }
});

// ✅ AFTER: Comparator.comparing() — reads like plain English
employees.sort(Comparator.comparing(Employee::getLastName));

// Chaining multiple sort criteria:
employees.sort(
    Comparator.comparing(Employee::getDepartment)
              .thenComparing(Employee::getLastName)
              .thenComparingInt(Employee::getAge)
);

// Reversed order:
employees.sort(Comparator.comparing(Employee::getSalary).reversed());
```

**Benefit:** Fluent, readable, and composable. Multi-key sorting that used to require complex nested logic is now a single chained expression. Null-safe variants like `Comparator.nullsFirst()` are also available.

***

## Quick Reference Summary

| # | Old Pattern | Modern Java Feature | Since |
|---|-------------|---------------------|-------|
| 1 | Anonymous class | Lambda expression | Java 8 |
| 2 | Imperative for-loop | Stream API | Java 8 |
| 3 | Null checks | `Optional<T>` | Java 8 |
| 4 | Data POJO class | `record` | Java 16 |
| 5 | String `+` concatenation | Text Blocks | Java 15 |
| 6 | `switch` + `break` | Switch Expression | Java 14 |
| 7 | `instanceof` + cast | Pattern Matching `instanceof` | Java 16 |
| 8 | Open inheritance | Sealed Classes | Java 17 |
| 9 | Manual Map building | `groupingBy` / `toMap` | Java 8 |
| 10 | Anonymous Comparator | `Comparator.comparing()` | Java 8 |

> **Pro tip:** Items 4, 7, and 8 (Records, Pattern Matching, and Sealed Classes) are designed to work together. A sealed interface + record subtypes + switch expression pattern match is the modern Java idiom for modelling domain types — it's concise, exhaustive, and compiler-verified.

***

## Quick Review Checklist

Use this list during code reviews:

* [ ] Replace anonymous functional classes with lambdas
* [ ] Replace imperative loops with Stream pipelines where appropriate
* [ ] Replace repeated null checks with Optional
* [ ] Convert DTO/value classes to records
* [ ] Replace multi-line string concatenation with text blocks
* [ ] Convert traditional switch statements to switch expressions
* [ ] Replace instanceof + cast with pattern matching
* [ ] Restrict inheritance using sealed classes when the hierarchy is fixed
* [ ] Replace manual Map creation with groupingBy() or toMap()
* [ ] Replace Comparator anonymous classes with Comparator.comparing()
* [ ] Prefer method references (`User::getName`) over simple lambdas
* [ ] Prefer `Stream.toList()` over `collect(Collectors.toList())` when mutability is not required
* [ ] Use records + sealed classes together for domain modeling
* [ ] Use switch expressions together with sealed hierarchies for exhaustive handling

This checklist covers many of the highest-impact modernization opportunities when refactoring legacy Java 8 code toward Java 21 idioms.

***

## My Personal Notes

### Refactoring items I am most confident about
- Lambda expression
- Stream API
- Record
- Text Blocks
- Switch Expression
- Pattern Matching for `instanceof`
- Sealed Classes

### Items I still need more practice on
- `Optional<T>`
- `groupingBy` / `toMap`
- `Comparator.comparing()`

### Most impactful refactoring (in my opinion)
- Stream API: Less boilerplate, easy to read and chain that the same actions would be complicated in looping.	

### Quick reference: which feature to use when
| Scenario | Feature to use |
|----------|---------------|
| Data carrier object with no logic | Record |
| All possible subtypes known at compile time | Sealed Class |
| Multi-step data transformation | Stream pipeline |
| Value might be absent | Optional |
| Multi-line string (JSON/SQL/HTML) | Text Block |
| Type-based branching | Switch Pattern Matching |