# Week 2 Summary

## Weekly Learning Goal Review
Master the core new features of Java 17/21, and be able to identify and write modern Java-style code.

## Features Mastered This Week

### Text Blocks (Java 15+)
- Purpose: it is useful for structured data like JSON, HTML and SQL without the need for escape character and concatenation and it simplifies the creation and increases the readability of multi-line string
- Most commonly use scenarios: JSON, SQL, HTML templates
- Dynamic content with `.formatted()`

### Records (Java 16+)
- Purpose: it simplifies the creation of data-carrying classes by automatically generating methods like equals(), hashCode(), and toString() and ensures that holding data is immutable.
- Most important application: DTO design for REST APIs
- Validation uses of compact constructors

### Sealed Classes (Java 17+)
- Purpose: it restricts other classes to extend it and allows only a set of permitted sub-classes. This approach improves code safety and readability and useful in domain modelling
- Power of Switch Pattern Matching
- Design of IT Ticket state flow models

### Advanced Stream Collectors
- `groupingBy` + downstream collectors
- `partitioningBy`
- `toMap` + mergeFunction
- `joining`

### Switch Expressions (Java 14–21)
- Fundamental differences between statement and expression
- Use cases for `yield`
- Guarded Patterns (with `when` condition)
- Null handling

## Files Created This Week

| Files | Main Content |
|------|---------|
| Week2Monday.java | Text Blocks + Records DTO Design |
| TicketStatus.java | Sealed Interface + Records State Model |
| Week2Tuesday.java | Pattern Matching In Three Levels |
| LegacyEmployeeUtils | Java 1.4 Style Code (Before Refactoring) |
| Week2Wednesday.java | Stream Refactoring + Collectors Advanced Usage |
| Week2Thursday.java | Switch Expressions In Full Version |
| LegacyHRSystem.java | Java 8 Style Code (Before Refactoring) |
| Week2Friday.java | Java 8→21 Comprehensive Refactoring |

## Friday's Refactoring Exercise AI Scoring
| Refactor | Score | What you did well (EN) | Better / sharper alternatives (EN) |
|----------|:-----:|-------------------------|-------------------------------------|
| **Employee → `record` + `EmployType` enum** | **5** | Record removes boilerplate; enum centralizes allowance **rates** and removes stringly-typed bugs. | Rename `EmployType` → `EmploymentType` for clarity; compact canonical constructor on enum is optional polish. |
| **Method 1 — high earners** | **5** | Clear `filter` → `map` → `toList()`; you avoided AI’s `.sorted()` (would change order). | For empty input, same as before; optional `Comparator` only if product needs sorted names. |
| **Method 2 — `groupingBy`** | **5** | Idiomatic one-liner; explicit `Collectors.toList()` is clear. | `groupingBy(Employee::department)` alone is enough (default `ArrayList`); use `LinkedHashMap` supplier only if you need stable dept order in demos. |
| **Method 3 — allowance** | **5** | **Better than the original TODO:** policy lives on `EmployType.getAllowance()`, not duplicated magic numbers. | Keep commented switch in notes only; in interview, articulate *enum method vs switch expression* trade-off. |
| **Method 4 — JSON text block** | **4** | Text block + `formatted()` is readable; field set still matches core data. | **Output shape changed:** pretty multi-line JSON vs legacy one-liner; `%.1f` changes salary display. For parity, match legacy format or document as intentional. Real apps: Jackson/`JsonWriter`, escaping. |
| **Method 5 — top earner per dept** | **4** | Correct `toMap` + merge pattern; `Function.identity()` is clean. | **Tie-break differs from legacy:** `>` keeps **later** employee on equal salary; legacy kept **first**. Prefer `>=` with `(a,b)->a` or `groupingBy` + `maxBy(Comparator.comparing(Employee::salary))` for clearer intent. |
| **`main` demo loops** | **3** | Some `forEach` modernization; method 1 uses stream print nicely. | Method 2: `IntStream.range` is harder than `l.stream().map(Employee::name).collect(joining(", "))` (you already had this in comments). Method 3: `printf` with format aligns with JSON decimals. Test data: `List.of(...)` if immutable. |

## Areas for Improvement
**All methods should be `static`:**
Every method in `Week2Friday` operates purely on its parameters and holds no instance state. Creating `Week2Friday system = new Week2Friday()` in `main` just to call instance methods is unnecessary object instantiation. Making them `static` also means `main` can call them directly.

**Printing Method 2 uses `IntStream` unnecessarily in `main`:**
`IntStream.range(0, l.size())` with manual comma logic is more complex than the legacy loop it replaced. `Collectors.joining(", ")` is the idiomatic replacement.

**`System.out.printf()` for formatted output in `main`:**
Methods 3 and 5 print formatted numbers with `System.out.println()` and string concatenation. `System.out.printf()` with format specifiers (`%.1f`, `%n`) is cleaner and avoids the automatic `double`-to-`String` conversion that can produce output like `85000.0` instead of `85,000.00`.

**Using `var` in `main for local variable type inference.**
```
// Current — verbose
List<Employee> employees = new ArrayList<>();

// With var — type is obvious from right-hand side
var employees = new ArrayList<Employee>();
```

## This Week's Biggest Takeaway
**Tell, Don’t Ask** principle: *tell objects what to do* rather than *asking them for data and acting on it* outside of the object. Always moving behaviour into objects alongside their data, keeping logic and state together.

## Saturday Self-Test Results

### Score for Each Question (1–10)
- Question 1 (BiFunction / Lambda): 9/10
- Question 2 (Stream Integration): 9/10
- Question 3 (Records + Sealed Classes): 7/10
- Question 4 (Switch + Text Blocks): 8/10
- Question 5 (Integrated Design): 6/10
- **Average Score: 7.8**

### Overall AI Assessment
**Ready for Week 3 Spring Boot?** Yes, clearly understand and demonstrated Java fundamentals that consists of streams, records, sealed types, switch expressions, optionals, and functional interfaces.

### Areas for Improvement on Sunday
**What to strengthen next?** Focus on data modeling consistency (like Q3), edge cases (duplicate keys in toMap, null/invalid inputs), and producing precise outputs (text block whitespace, formatting). Also practice “domain objects + services” separation—this maps directly to Spring Boot layering.

### Confidence Level Before Week 3
7 / 10

## Next Week's Plan
Week 3: The basic of Spring Boot 3.x — REST API